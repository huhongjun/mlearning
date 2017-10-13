//
//  LoginViewController.m
//  mLearning
//
//  Created by zhijin on 14-3-28.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import "LoginViewController.h"
#import "ListViewController.h"
#import "Ton.h"

@interface LoginViewController ()
//账号记录路径
@property (copy , nonatomic) NSString * filePath;
//登陆页面textfield
@property (retain, nonatomic) UITextField * textfield;
//登陆界面登陆按钮
@property (retain, nonatomic) UIButton * btn;

@end

@implementation LoginViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    //设置NAV
    [self.navigationController setNavigationBarHidden:YES];
    self.navigationController.navigationItem.backBarButtonItem.title = @"";
    self.view.backgroundColor = [UIColor colorWithRed:100.0/255.0 green:205.0/255.0 blue:155.0/255.0 alpha:1];
    //账号登陆文件的路径
    NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDirectory = [paths objectAtIndex:0];
    NSString * member = [NSString stringWithFormat:@"memberID.plist"];
    self.filePath = [NSString stringWithFormat:@"%@/%@",documentDirectory,member];
    //开始加载
    [self viewload];
}
//延时加载
-(void)viewload
{
    //textfield设置
    self.textfield = [[UITextField alloc]initWithFrame:CGRectMake(110*2, 200*2, 120*2, 25*2)];
    NSArray * aar = [[NSArray alloc] initWithContentsOfFile:self.filePath];
    self.textfield.text = [aar objectAtIndex:0];
    self.textfield.backgroundColor = [UIColor whiteColor];
    self.textfield.keyboardType = UIKeyboardTypeEmailAddress;
    self.btn = [UIButton buttonWithType:UIButtonTypeCustom];
    //button设置
    self.btn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    self.btn.frame = CGRectMake(230*2, 198*2, 50*2, 30*2);
    [self.btn setBackgroundImage:[UIImage imageNamed:@"登录按钮.png"] forState:UIControlStateNormal];
    [self.btn addTarget:self action:@selector(loginbutton) forControlEvents:UIControlEventTouchUpInside];
    //Logo的imageView设置
    UIImageView * imageV = [[UIImageView alloc]initWithFrame:CGRectMake(110*2, 120*2, 170*2, 70*2)];
    imageV.image = [UIImage imageNamed:@"logo.png"];
    imageV.backgroundColor = [UIColor clearColor];
    //加载控件
    [self.view addSubview:imageV];
    [self.view addSubview:self.textfield];
    [self.view addSubview:self.btn];
}
//button way
-(void)loginbutton
{
    NSString * str = self.textfield.text;
    //邮箱认证
    BOOL login = NO;
    if ([str rangeOfString:@"@"].length>0) {
        NSArray * arr = [str componentsSeparatedByString:@"@"];
        NSString * string = [arr objectAtIndex:1];
        if ([string rangeOfString:@"."].length>0 ) {
            NSArray * arr1 = [string componentsSeparatedByString:@"."];
            if (![[arr1 objectAtIndex:0]isEqualToString:@""]) {
                if ([[arr1 objectAtIndex:1]isEqualToString:@"com"]) {
                    login = YES;
                }else{
                    login = NO;
                }
            }else{
                login = NO;
            }
        }else{
            login = NO;
        }
    }else{
        login = NO;
    }
    //认证成功记录memberID.跳转页面
    if (self.textfield.text != nil && login ==YES) {
        Ton * ton = [Ton sharedOrder];
        ton.memberID = self.textfield.text;
        [self.btn setBackgroundImage:[UIImage imageNamed:@"登陆按钮按下.png"] forState:UIControlStateNormal];
        ListViewController * listVC = [[ListViewController alloc]init];
        NSArray * arry = [NSArray arrayWithObject:self.textfield.text];
        [arry writeToFile:self.filePath atomically:YES];
        [self.navigationController pushViewController:listVC animated:YES];
    }
    //认证失败，账号为空（Alert提示）
    else if (self.textfield.text == nil) {
        UIAlertView * alertview = [[UIAlertView alloc] initWithTitle:@"提示" message:@"您输入的账号为空" delegate:self cancelButtonTitle:@"重新输入" otherButtonTitles:Nil, nil];
        [alertview show];
    }
    //认证失败，邮箱格式（Alert提示）
    else{
        UIAlertView * alertview = [[UIAlertView alloc] initWithTitle:@"提示" message:@"邮箱格式不正确" delegate:self cancelButtonTitle:@"重新输入" otherButtonTitles:Nil, nil];
        [alertview show];
    }

}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];

}

@end
