//
//  AppDelegate.h
//  mLearning
//
//  Created by zhijin on 14-3-28.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ListViewController;
@class LoginViewController;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) ListViewController * listVC;
@property (strong, nonatomic) LoginViewController * loginVC;

@end
