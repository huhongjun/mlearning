//
//  DetailsViewController.m
//  mLearning
//
//  Created by zhijin on 14-3-31.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import "DetailsViewController.h"

@interface DetailsViewController ()

@property (copy  , nonatomic) NSString * filePath;
@property (retain, nonatomic) NSArray * section;
@end

@implementation DetailsViewController

@synthesize arequest;
@synthesize moviePlayer;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"课程详情";
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
    //如果路径中没有此ID记录则创建相应plist文件
    NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDirectory = [paths objectAtIndex:0];
    NSLog(@"documentDirectory : %@",documentDirectory);
    Ton * ton = [Ton sharedOrder];
    self.filePath = [NSString stringWithFormat:@"%@/%@.plist",documentDirectory,ton.memberID];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL result = [fileManager fileExistsAtPath:self.filePath];
    if (result == NO) {
        NSString * string1 = @"视频名称";
        NSString * string2 = @"播放时间";
        NSArray * array = [NSArray arrayWithObjects:string1,string2,nil];
        NSMutableArray * mutablearray = [NSMutableArray arrayWithCapacity:10];
        [mutablearray addObject:array];
        [mutablearray writeToFile:self.filePath atomically:YES];
        NSLog(@"写入成功");
    }
    //NAV设置
    UIButton * backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    backBtn.frame = CGRectMake(0.0, 100.0, 50.0, 50.0);
    [backBtn setTitle:@"" forState:UIControlStateNormal];
    [backBtn setBackgroundImage:[UIImage imageNamed:@"返回.png"] forState:UIControlStateNormal];
    [backBtn addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *backBarBtn = [[UIBarButtonItem alloc] initWithCustomView:backBtn];
    self.navigationItem.leftBarButtonItem=backBarBtn;
    self.navigationController.navigationItem.hidesBackButton = YES;
    //加载
    [self newloadview];
}
//延时加载
-(void)newloadview
{
    Ton * ton = [Ton sharedOrder];
    self.section = [[NSArray alloc]init];
    for (NSArray * array in ton.SectionArray) {
        if ([[array objectAtIndex:0]isEqualToString:ton.SectionID]) {
            self.section = array;
        }
    }
    //UIImageView
    UIImageView * imageV =[[UIImageView alloc]initWithFrame:CGRectMake(10*2, 10*2, 100*2, 75*2)];
    imageV.image = [UIImage imageNamed:@"课程图片.png"];
    //标题UILabel
    self.sectionlabel = [[UILabel alloc]initWithFrame:CGRectMake(125*2, 10*2, 150*2, 18*2)];
    self.sectionlabel.backgroundColor = [UIColor clearColor];
    self.sectionlabel.textColor = [UIColor grayColor];
    self.sectionlabel.textAlignment = NSTextAlignmentCenter;
    self.sectionlabel.text = [self.section objectAtIndex:1];
    //时长UILabel
    self.timelabel = [[UILabel alloc]initWithFrame:CGRectMake(125*2, 30*2, 150*2, 13*2)];
    self.timelabel.backgroundColor = [UIColor clearColor];
    self.timelabel.textColor = [UIColor lightGrayColor];
    NSString * str = [NSString stringWithFormat:@"时长 : %@",[self.section objectAtIndex:4]];
    self.timelabel.text = str;
    //视频播放UIButton
    UIButton * btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.frame = CGRectMake(230*2, 130, 70*2, 30*2);
    [btn setBackgroundImage:[UIImage imageNamed:@"播放按钮.png"] forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(movieplay) forControlEvents:UIControlEventTouchUpInside];
    //简述UITextView
    UITextView * textV = [[UITextView alloc]initWithFrame:CGRectMake(10*2, 105*2, 300*2, (365-64)*2)];
    textV.text = [self.section objectAtIndex:3];
    textV.textColor = [UIColor lightGrayColor];
    textV.font = [UIFont systemFontOfSize:25];
    textV.editable=NO;
    //加载
    [self.view addSubview:imageV];
    [self.view addSubview:self.sectionlabel];
    [self.view addSubview:self.timelabel];
    [self.view addSubview:btn];
    [self.view addSubview:textV];
}

//视频断点续播
-(void)movieLoadStateChange:(NSNotification *)notification
{
    NSMutableArray * mutablearray = [[NSMutableArray alloc] initWithContentsOfFile:self.filePath];
    for (NSArray * arr in mutablearray) {
        if ([[arr objectAtIndex:0]isEqualToString:[self.section objectAtIndex:1]]) {
            NSString * thistimestring = [arr objectAtIndex:1];
            NSTimeInterval thistime = [thistimestring intValue];
            [moviePlayer.moviePlayer setCurrentPlaybackTime: thistime];
//            [self presentMoviePlayerViewControllerAnimated:moviePlayer];
        }
    }
    
}
//NAV返回
-(void)backAction
{
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
//    [moviePlayer.moviePlayer play];
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
//POST的请求回调
- (void)requestFinished:(ASIHTTPRequest *)request
{
    NSLog(@"收到返回数据");
    NSData *data =  [request responseData];
    NSDictionary * tionary  = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
    int respCode = [[tionary objectForKey:@"respCode"] intValue];
    if (respCode == 1) {
        NSLog(@"请求成功");
    }else{
        NSLog(@"请求失败");
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    
}

@end
