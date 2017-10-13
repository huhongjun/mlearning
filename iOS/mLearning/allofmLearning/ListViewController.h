//
//  ListViewController.h
//  mLearning
//
//  Created by zhijin on 14-3-28.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ListViewController : UIViewController<UITableViewDelegate,UITableViewDataSource,NSXMLParserDelegate>

{
    NSXMLParser* xml_Parser;
    NSMutableArray * courseMutableArray;
    NSMutableArray * chapterMutableArray;
    NSMutableArray * sectionMutableArray;
}

@property (strong, nonatomic) NSXMLParser* xml_Parser;
@property (strong, nonatomic) NSMutableArray * courseMutableArray;
@property (strong, nonatomic) NSMutableArray * chapterMutableArray;
@property (strong, nonatomic) NSMutableArray * sectionMutableArray;
@property (strong, nonatomic) NSString* xml_TempStr;//保存临时数据

@end
