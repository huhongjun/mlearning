//
//  SectionViewController.h
//  mLearning
//
//  Created by zhijin on 14-3-31.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SectionViewController : UIViewController<UITableViewDataSource,UITableViewDelegate>

@property (retain , nonatomic) NSMutableArray * resultmutablearray;

@end
