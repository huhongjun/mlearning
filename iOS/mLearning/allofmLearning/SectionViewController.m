//
//  SectionViewController.m
//  mLearning
//
//  Created by zhijin on 14-3-31.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import "SectionViewController.h"
#import "Ton.h"
#import "DetailsViewController.h"
#import "SectionCell.h"

@interface SectionViewController ()

@end

@implementation SectionViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        Ton * ton = [Ton sharedOrder];
        for (NSArray * array in ton.ChapterArray) {
            if ([[array objectAtIndex:0]isEqualToString:ton.ChapterID]) {
                self.title = [array objectAtIndex:1];
            }
        }
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	self.view.backgroundColor = [UIColor whiteColor];
    //NAV设置
    UIButton * backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    backBtn.frame = CGRectMake(0.0, 100.0, 50.0, 50.0);
    [backBtn setTitle:@"" forState:UIControlStateNormal];
    [backBtn setBackgroundImage:[UIImage imageNamed:@"返回.png"] forState:UIControlStateNormal];
    [backBtn addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *backBarBtn = [[UIBarButtonItem alloc] initWithCustomView:backBtn];
    self.navigationItem.leftBarButtonItem=backBarBtn;
    self.navigationController.navigationItem.hidesBackButton = YES;
    //页面数据初始化
    self.resultmutablearray = [NSMutableArray arrayWithCapacity:10];
    self.view.backgroundColor = [UIColor whiteColor];
    Ton * ton = [Ton sharedOrder];
    for (NSArray * arr in ton.SectionArray) {
        NSString * str = [arr objectAtIndex:0];
        if ([str hasPrefix:ton.ChapterID]) {
            [self.resultmutablearray addObject:arr];
        }
    }
    //加载
    [self newloadview];
}
//延时加载
-(void)newloadview
{
    UITableView * tab = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.bounds.size.width, (190*self.resultmutablearray.count)+64)];
    tab.dataSource = self;
    tab.delegate   = self;
    tab.separatorColor = [UIColor lightGrayColor];
    [self.view addSubview:tab];
}

-(void)backAction
{
    [self.navigationController popViewControllerAnimated:YES];
}



#pragma mark- UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.resultmutablearray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString * indefi = @"cell";
    SectionCell * cell = [tableView dequeueReusableCellWithIdentifier:indefi];
    if (!cell) {
        cell = [[SectionCell alloc]init];
    }
    NSString * str = [NSString stringWithFormat:@"%@",[[self.resultmutablearray objectAtIndex:indexPath.row]objectAtIndex:1]];
    cell.sectionlabel.text = str;
    cell.sectionlabel.textColor = [UIColor blackColor];
    cell.label.text = [[self.resultmutablearray objectAtIndex:indexPath.row]objectAtIndex:3];
    NSString * TEXT = [[self.resultmutablearray objectAtIndex:indexPath.row]objectAtIndex:4];
    cell.timelabel.text = [NSString stringWithFormat:@"时长 : %@",TEXT];
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 190.0;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    Ton * ton = [Ton sharedOrder];
    ton.SectionID = [[self.resultmutablearray objectAtIndex:indexPath.row]objectAtIndex:0];
    DetailsViewController * detailsVC = [[DetailsViewController alloc]init];
    [self.navigationController pushViewController:detailsVC animated:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
