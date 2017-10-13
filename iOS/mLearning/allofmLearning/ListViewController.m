//
//  ListViewController.m
//  mLearning
//
//  Created by zhijin on 14-3-28.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import "ListViewController.h"
#import "Ton.h"
#import "SectionViewController.h"
#import "LearningRecordViewController.h"

#define LISTVIEWTITTLE @"Android培训课程"

@interface ListViewController ()

@end

@implementation ListViewController
@synthesize sectionMutableArray;
@synthesize chapterMutableArray;
@synthesize courseMutableArray;
@synthesize xml_Parser;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        courseMutableArray  = [NSMutableArray arrayWithCapacity:10];
        chapterMutableArray = [NSMutableArray arrayWithCapacity:10];
        sectionMutableArray = [NSMutableArray arrayWithCapacity:10];
        self.title = LISTVIEWTITTLE;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
//NAV设置
    [self.navigationController setNavigationBarHidden:NO];
    //NAV左侧自定义
    UIButton * backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    backBtn.frame = CGRectMake(0.0, 100.0, 62.0, 29.0);
    [backBtn setTitle:@"" forState:UIControlStateNormal];
    [backBtn setBackgroundImage:[UIImage imageNamed:@""] forState:UIControlStateNormal];
    [backBtn addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *backBarBtn = [[UIBarButtonItem alloc] initWithCustomView:backBtn];
    self.navigationItem.leftBarButtonItem=backBarBtn;
    self.navigationController.navigationItem.hidesBackButton = YES;
    //NAV右侧自定义
    UIButton *indexBtn = [[UIButton alloc] initWithFrame:CGRectMake(250.0, 100.0, 50.0, 50.0)];
    [indexBtn setTitle:@"" forState:UIControlStateNormal];
    [indexBtn setBackgroundImage:[UIImage imageNamed:@"学习记录.png"] forState:UIControlStateNormal];
    [indexBtn addTarget:self action:@selector(goIndex) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *indexBarBtn = [[UIBarButtonItem alloc] initWithCustomView:indexBtn];
    self.navigationItem.rightBarButtonItem = indexBarBtn;
    self.navigationController.view.backgroundColor = [UIColor colorWithRed:100.0/255.0 green:205.0/255.0 blue:155.0/255.0 alpha:1];
    self.view.backgroundColor = [UIColor whiteColor];
    //XML解析路径
    NSString* textPath=[[NSBundle mainBundle]pathForResource:@"course_build" ofType:@"xml"];
    NSURL* textPathURL=[NSURL fileURLWithPath:textPath];
    xml_Parser=[[NSXMLParser alloc]initWithContentsOfURL:textPathURL];
    xml_Parser.delegate=self;
    BOOL flag =  [xml_Parser parse];
    if (flag) {
        NSLog(@"文件加载成功");
    }else{
        NSError * error = [xml_Parser parserError];
        NSLog(@"文件加载失败:%@",error);
    }
}
//NAV左
-(void)backAction
{
    
}
//NAV右
-(void)goIndex
{
    LearningRecordViewController * learningrecordVC = [[LearningRecordViewController alloc]init];
    [self.navigationController pushViewController:learningrecordVC animated:YES];
}
//加载
-(void)newloadview
{
    Ton * ton = [Ton sharedOrder];
    UITableView * tab = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.bounds.size.width, (88*ton.ChapterArray.count)+64)];
    tab.dataSource = self;
    tab.delegate = self;
    tab.separatorColor = [UIColor lightGrayColor];
    [self.view addSubview:tab];
}

#pragma mark- NSXMLParserDelegate

-(void)parserDidStartDocument:(NSXMLParser *)parser
{
    NSLog(@"开始解析\n");
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict
{
    if ([elementName isEqualToString:@"course"]) {
        NSString * courseID = [attributeDict valueForKey:@"id"];
        NSString * courseName = [attributeDict valueForKey:@"name"];
        NSArray * coursearray = [NSArray arrayWithObjects:courseID,courseName,nil];
        [courseMutableArray addObject:coursearray];
    }
    if ([elementName isEqualToString:@"chapter"]) {
        NSString * chapterID = [attributeDict valueForKey:@"id"];
        NSString * chapterName = [attributeDict valueForKey:@"name"];
        NSArray * chapterarray = [NSArray arrayWithObjects:chapterID,chapterName,nil];
        [chapterMutableArray addObject:chapterarray];
    }
}

-(void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
    self.xml_TempStr = string;
}

-(void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName
{
    Ton * ton = [Ton sharedOrder];
    ton.CourseArray  = courseMutableArray;
    ton.ChapterArray = chapterMutableArray;
//    ton.SectionArray = sectionMutableArray;
    if ([elementName isEqualToString:@"id"]){
        [sectionMutableArray addObject:self.xml_TempStr];
    }
    else if ([elementName isEqualToString:@"name"]){
        [sectionMutableArray addObject:self.xml_TempStr];
    }
    else if([elementName isEqualToString:@"url"]){
        [sectionMutableArray addObject:self.xml_TempStr];
    }
    else if([elementName isEqualToString:@"desc"]){
        [sectionMutableArray addObject:self.xml_TempStr];
    }
    else if([elementName isEqualToString:@"length"]){
        [sectionMutableArray addObject:self.xml_TempStr];
        NSString * str1 = [sectionMutableArray objectAtIndex:0];
        NSString * str2 = [sectionMutableArray objectAtIndex:1];
        NSString * str3 = [sectionMutableArray objectAtIndex:2];
        NSString * str4 = [sectionMutableArray objectAtIndex:3];
        NSString * str5 = [sectionMutableArray objectAtIndex:4];
        NSArray * arr = [NSArray arrayWithObjects: str1 , str2 ,str3 , str4 , str5 , nil];
        [ton.SectionArray addObject:arr];
        [sectionMutableArray removeAllObjects];
    }
}

-(void)parserDidEndDocument:(NSXMLParser *)parser
{
    NSLog(@"解析结束\n");
    [self newloadview];
}

#pragma mark- UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    Ton * ton = [Ton sharedOrder];
    return ton.ChapterArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString * indefi =@"cell";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:indefi];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:indefi];
    }
    Ton * ton = [Ton sharedOrder];
    UIView *aView = [[UIView alloc] initWithFrame:cell.contentView.frame];
    aView.backgroundColor = [UIColor grayColor];
    cell.selectedBackgroundView = aView;
    NSString * str = [[ton.ChapterArray objectAtIndex:indexPath.row]objectAtIndex:1];
    cell.textLabel.text      = str;
    cell.textLabel.textColor = [UIColor lightGrayColor];
    cell.textLabel.font = [UIFont systemFontOfSize:30];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    Ton * ton = [Ton sharedOrder];
    NSString * idnumber = [[ton.ChapterArray objectAtIndex:indexPath.row]objectAtIndex:0];
    ton.ChapterID = idnumber;
    SectionViewController * sectionVC = [[SectionViewController alloc]init];
    [self.navigationController pushViewController:sectionVC animated:YES];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 88.0;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
