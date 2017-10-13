//
//  LearningRecordViewController.m
//  mLearning
//
//  Created by zhijin on 14-3-31.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import "LearningRecordViewController.h"

@interface LearningRecordViewController ()

@property (copy   , nonatomic) NSString * filePath;
@property (retain , nonatomic) NSArray * section;
@property (retain , nonatomic) UIActivityIndicatorView * activity;
@property (retain , nonatomic) NSMutableArray * resultarray;
@property (retain , nonatomic) NSMutableArray * cellarray;
@property (retain , nonatomic) UITableView * tab;
@property (assign , nonatomic) int sec;
@end

@implementation LearningRecordViewController

@synthesize myrequest;
@synthesize arequest;
@synthesize moviePlayer;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"学习记录";
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(playDidChangeNotification:) name:MPMoviePlayerPlaybackStateDidChangeNotification object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	//监听
    NSNotificationCenter *notification=[NSNotificationCenter defaultCenter];
    [notification addObserver:self selector:@selector(movieLoadStateChange:) name:MPMoviePlayerReadyForDisplayDidChangeNotification object:moviePlayer];
    //        MPMoviePlayerPlaybackStateDidChangeNotification
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    self.resultarray = [[NSMutableArray alloc]initWithCapacity:10];
    self.cellarray = [[NSMutableArray alloc]initWithCapacity:10];

    
    NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDirectory = [paths objectAtIndex:0];
    NSLog(@"documentDirectory : %@",documentDirectory);
    Ton * ton = [Ton sharedOrder];
    self.filePath = [NSString stringWithFormat:@"%@/%@.plist",documentDirectory,ton.memberID];
    //NAV设置
    UIButton * backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    backBtn.frame = CGRectMake(0.0, 100.0, 50.0, 50.0);
    [backBtn setTitle:@"" forState:UIControlStateNormal];
    [backBtn setBackgroundImage:[UIImage imageNamed:@"返回.png"] forState:UIControlStateNormal];
    [backBtn addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *backBarBtn = [[UIBarButtonItem alloc] initWithCustomView:backBtn];
    self.navigationItem.leftBarButtonItem=backBarBtn;
    self.navigationController.navigationItem.hidesBackButton = YES;
    //有网的状态下读取服务器记录
    if ([self isConnectionAvailable] == NO) {
        Ton * ton = [Ton sharedOrder];
        NSString * string = [NSString stringWithFormat:@"http://192.168.0.140:8080/jeresydemonew/rest/courseRes/getCourseProgress/%@",ton.memberID];
        NSURL *url = [NSURL URLWithString:string];
        myrequest = [ASIHTTPRequest requestWithURL:url];
        [myrequest setDelegate:self];
        [myrequest startAsynchronous];
        self.activity = [[UIActivityIndicatorView alloc]initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
        [self.activity startAnimating];
    }
    //没有网络状态下读取本地记录
    if ([self isConnectionAvailable] == YES) {
        UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"提示" message:@"当前无可用网络将使用本地记录数据，无法同步记录" delegate:self cancelButtonTitle:@"知道了" otherButtonTitles:Nil, nil];
        [alert show];
        NSFileManager *fileManager = [NSFileManager defaultManager];
        BOOL result = [fileManager fileExistsAtPath:self.filePath];
        //如果没有此ID记录文件则创建相应的ID记录文件
        if (result == NO) {
            NSString * string1 = @"名称";
            NSString * string2 = @"播放时间";
            NSArray * array = [NSArray arrayWithObjects:string1,string2,nil];
            NSMutableArray * mutablearray = [NSMutableArray arrayWithCapacity:10];
            [mutablearray addObject:array];
            [mutablearray writeToFile:self.filePath atomically:YES];
            NSLog(@"写入成功");
            [self.resultarray removeAllObjects];
            self.resultarray = [[NSMutableArray alloc] initWithContentsOfFile:self.filePath];
        }
        //当本地此ID记录为空的时候
        else {
            self.resultarray = [[NSMutableArray alloc] initWithContentsOfFile:self.filePath];
            if (self.resultarray.count == 1) {
                [self.resultarray removeAllObjects];
            }
        }
        [self newloadview];
    }
}
//延时加载
-(void)newloadview
{
    self.tab = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.bounds.size.width, (88*self.resultarray.count)+64)];
    self.tab.dataSource = self;
    self.tab.delegate = self;
    self.tab.separatorColor = [UIColor lightGrayColor];
    [self.view addSubview:self.tab];
}

-(void)movieLoadStateChange:(NSNotification *)notification
{
    NSMutableArray * mutablearray = [[NSMutableArray alloc] initWithContentsOfFile:self.filePath];
    for (NSArray * arr in mutablearray) {
        if ([[arr objectAtIndex:0]isEqualToString:[self.section objectAtIndex:1]]) {
            NSString * thistimestring = [arr objectAtIndex:1];
            NSTimeInterval thistime = [thistimestring intValue];
            [moviePlayer.moviePlayer setCurrentPlaybackTime: thistime];
        }
    }
}
//NAV返回
-(void)backAction
{
    [self.resultarray removeAllObjects];
    [arequest clearDelegatesAndCancel];
    [self.navigationController popViewControllerAnimated:YES];
}
//视频播放
-(void)movieplay
{
    [[AVAudioSession sharedInstance] setCategory: AVAudioSessionCategoryPlayback error: nil];
    NSString * str = [self.section objectAtIndex:2];
    NSString * resultstr = [NSString stringWithFormat:@"/Documents/course/video/%@",str];
    NSString * path = [NSHomeDirectory() stringByAppendingString:resultstr];
    NSURL * url = [NSURL fileURLWithPath:path];
    moviePlayer = [[MPMoviePlayerViewController alloc] initWithContentURL:url];
    [self presentMoviePlayerViewControllerAnimated:moviePlayer];
}
//视频状态监听
- (void)playDidChangeNotification:(NSNotification *)notification {
    MPMoviePlayerController *moviePlayer1 = notification.object;
    MPMoviePlaybackState playState = moviePlayer1.playbackState;
    if (playState == MPMoviePlaybackStateStopped) {
        //保存本地记录
        Ton * ton = [Ton sharedOrder];
        NSTimeInterval aTimer = moviePlayer1.currentPlaybackTime ;
        ton.time = aTimer;
        for (NSArray * array in ton.SectionArray) {
            if ([[array objectAtIndex:0]isEqualToString:ton.SectionID]) {
                self.section = array;
            }
        }
        NSString * aTimerstring = [NSString stringWithFormat:@"%f",aTimer];
        NSArray * writearray = [NSArray arrayWithObjects:[self.section objectAtIndex:1],aTimerstring,[self.section objectAtIndex:0],nil];
        NSMutableArray * mutablearray = [[NSMutableArray alloc] initWithContentsOfFile:self.filePath];
        for (int i=0; i<[mutablearray count]; i++) {
            NSArray * arr = [mutablearray objectAtIndex:i];
            if ([[arr objectAtIndex:0] isEqualToString:[self.section objectAtIndex:1]]) {
                [mutablearray removeObjectAtIndex:i];
            }
        }
        [mutablearray addObject:writearray];
        [mutablearray writeToFile:self.filePath atomically:YES];
        NSIndexPath *indexPath_1=[NSIndexPath indexPathForRow:1 inSection:0];
        NSArray *indexArray=[NSArray arrayWithObject:indexPath_1];
        [self.tab reloadRowsAtIndexPaths:indexArray withRowAnimation:UITableViewRowAnimationAutomatic];
        
        //一个section刷新
//        NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:self.sec];
//        [self.tab reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
        //一个cell刷新
//        NSIndexPath *indexPath=[NSIndexPath indexPathForRow:self.sec inSection:0];
//        [self.tab reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
//        [self.tab removeFromSuperview];
        //刷新tableview
        [self.cellarray removeAllObjects];
        self.resultarray = mutablearray;
        [self newloadview];
        
        //在有网络的状态下同步记录
        if ([self isConnectionAvailable] == YES)
        {
            NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
            NSString *documentDirectory = [paths objectAtIndex:0];
            Ton * ton = [Ton sharedOrder];
            NSString * filePath = [NSString stringWithFormat:@"%@/%@.plist",documentDirectory,ton.memberID];
            NSFileManager *fileManager = [NSFileManager defaultManager];
            BOOL result = [fileManager fileExistsAtPath:filePath];
            if (result == YES) {
                NSString * string = [NSString stringWithFormat:@"http://192.168.0.140:8080/jeresydemonew/rest/courseRes/saveCourseProgress"];
                NSURL *url = [NSURL URLWithString:string];
                NSMutableArray * resultarray = [[NSMutableArray alloc] initWithContentsOfFile:filePath];
                NSMutableDictionary * dic = [[NSMutableDictionary alloc]init];
                if (resultarray.count == 1) {
                    return;
                }
                //请求体中的JSON构建
                NSString * resultstr = @"[";
                for (int i = 0; i < resultarray.count; i++) {
                    if (i!=0) {
                        NSString * memberId = ton.memberID;
                        NSString * sectionId = [[resultarray objectAtIndex:i]objectAtIndex:2];
                        NSString * sectionProgress = [[resultarray objectAtIndex:i]objectAtIndex:1];
                        NSString * sectionName = [[resultarray objectAtIndex:i]objectAtIndex:0];
                        NSString * onesection = [NSString stringWithFormat:@"{\"sectionId\":\"%@\",\"sectionName\":\"%@\",\"memberId\":\"%@\",\"sectionProgress\":\"%@\"},",sectionId,sectionName,memberId, sectionProgress];
                        NSString * onestr = resultstr;
                        resultstr = [NSString stringWithFormat:@"%@%@",onestr,onesection];
                    }
                }
                NSString * zlystr = resultstr;
                resultstr = [NSString stringWithFormat:@"%@]",zlystr];
                [dic setObject:ton.memberID forKey:@"memberId"];
                [dic setObject:resultstr forKey:@"progressJson"];
                NSString * returnString = [dic JSONString];
                NSLog(@"请求体中的JSON ：%@",returnString);
                arequest = [ASIFormDataRequest requestWithURL:url];
                arequest.timeOutSeconds =10;
                [arequest addPostValue:returnString forKey:@"jsonParam"];
                NSLog(@"发送POST请求");
                [arequest setDelegate:self];
                [arequest startAsynchronous];
            }
        }
        
    } else if(playState == MPMoviePlaybackStatePlaying) {
        NSLog(@"播放");
    } else if(playState == MPMoviePlaybackStatePaused) {
        NSLog(@"暂停");
    }
}
//POST,GET的请求回调
- (void)requestFinished:(ASIHTTPRequest *)request
{
    NSData *data =  [request responseData];
    NSDictionary * tionary = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:nil];
    int respCode = [[tionary objectForKey:@"respCode"] intValue];
    if (respCode == 1) {
        NSLog(@"请求成功");
        if ([[tionary objectForKey:@"result"]objectForKey:@"progressJson"]) {
            [self.activity stopAnimating];
            NSDictionary * dic = [tionary objectForKey:@"result"];
            NSString * resparray = [dic objectForKey:@"progressJson"];
            NSData * da = [resparray dataUsingEncoding:NSUTF8StringEncoding];
            NSMutableArray * jsonary = [NSJSONSerialization JSONObjectWithData:da options:NSJSONReadingMutableLeaves error:nil];
            self.resultarray = jsonary;
            NSMutableArray * mutablearray = [[NSMutableArray alloc] initWithContentsOfFile:self.filePath];
            for (NSArray * arry1 in self.resultarray) {
                for (NSDictionary * arry2 in mutablearray) {
                    if ([[arry1 objectAtIndex:2]isEqualToString:[arry2 objectForKey:@"memberId"]]) {
                        [mutablearray removeObject:arry1];
                        NSArray * arry3 = [NSArray arrayWithObjects:[arry2 valueForKey:@"sectionName"],[arry2 valueForKey:@"sectionProgress"],[arry2 valueForKey:@"sectionId"],nil];
                        [mutablearray addObject:arry3];
                    }else{
                        [mutablearray addObject:arry2];
                    }
                }
            }
            [mutablearray writeToFile:self.filePath atomically:YES];
            [self newloadview];
        }else{
            return;
        }
    }else {
        UIAlertView * alertview2 = [[UIAlertView alloc] initWithTitle:@"提示" message:@"网络请求失败" delegate:self cancelButtonTitle:@"返回" otherButtonTitles:Nil, nil];
        [alertview2 show];
    }
}

//判断当前是否有可用网络
-(BOOL) isConnectionAvailable{
    
    BOOL isExistenceNetwork = YES;
    Reachability *reach = [Reachability reachabilityWithHostName:@"www.apple.com"];
    switch ([reach currentReachabilityStatus]) {
        case NotReachable:
            isExistenceNetwork = NO;
            break;
        case ReachableViaWiFi:
            isExistenceNetwork = YES;
            break;
        case ReachableViaWWAN:
            isExistenceNetwork = YES;
            break;
    }
    
    return isExistenceNetwork;
}


#pragma mark- UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.resultarray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString * indefi = @"cell";
    RecordCell * cell = [tableView dequeueReusableCellWithIdentifier:indefi];
    if (!cell) {
        cell = [[RecordCell alloc]init];
    }
    if ([self isConnectionAvailable] == NO) {
        NSDictionary * dictionary = [self.resultarray objectAtIndex:indexPath.row];
        NSArray * dictionaryallkey = [dictionary allKeys];
        for (NSString * key in dictionaryallkey) {
            if ([key isEqualToString:@"sectionName"]) {
                NSString * str1 = [dictionary objectForKey:@"sectionName"];
                cell.name.text = str1;
                cell.name.font = [UIFont systemFontOfSize:30];
                [self.cellarray addObject:str1];
            }
            if ([key isEqualToString:@"sectionProgress"]) {
                Ton * ton = [Ton sharedOrder];
                NSString *  str2 = [dictionary objectForKey:@"sectionProgress"];
                NSTimeInterval aTimer = [str2 intValue] ;
                int hour = (int)(aTimer/3600);
                int minute = (int)(aTimer - hour*3600)/60;
                int second = aTimer - hour*3600 - minute*60;
                ton.time = aTimer;
                NSString * dural = [NSString stringWithFormat:@"已观看至 %d时%d分%d秒",hour, minute,second];
                cell.time.text = [NSString stringWithFormat:@"%@",dural];
                cell.time.font = [UIFont systemFontOfSize:30];
                
            }
        }
    }else{
        NSString * str1 = [[self.resultarray objectAtIndex:indexPath.row]objectAtIndex:0];
        NSString * str2 = [[self.resultarray objectAtIndex:indexPath.row]objectAtIndex:1];
        NSTimeInterval aTimer = [str2 intValue] ;
        int hour = (int)(aTimer/3600);
        int minute = (int)(aTimer - hour*3600)/60;
        int second = aTimer - hour*3600 - minute*60;
        NSString * dural = [NSString stringWithFormat:@"已观看至 %d时%d分%d秒",hour, minute,second];
        if (indexPath.row == 0) {
            dural = @"播放时间";
        }
        cell.name.text = str1;
        [self.cellarray addObject:str1];
        cell.name.font = [UIFont systemFontOfSize:30];
        cell.time.text = dural;
        cell.time.font = [UIFont systemFontOfSize:30];
    }
    return cell;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 88.0;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0) {
        return;
    }else{
        Ton * ton = [Ton sharedOrder];
        for (NSArray * array in ton.SectionArray) {
            if ([[array objectAtIndex:1]isEqualToString:[self.cellarray objectAtIndex:indexPath.row]]) {
                self.section = array;
            }
        }
        self.sec = indexPath.row;
        [self movieplay];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
