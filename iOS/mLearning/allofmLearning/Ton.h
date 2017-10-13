//
//  Ton.h
//  mLearning
//
//  Created by zhijin on 14-3-28.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Ton : NSObject

//登陆ID
@property (copy  ,nonatomic) NSString * memberID;
//课程信息
@property (retain, nonatomic) NSArray * CourseArray;
//章信息
@property (retain, nonatomic) NSArray * ChapterArray;
//节信息
@property (retain, nonatomic) NSMutableArray * SectionArray;
//选中章ID
@property (copy  , nonatomic) NSString * ChapterID;
//选中节ID
@property (copy  , nonatomic) NSString * SectionID;
//播放时间
@property (assign, nonatomic) NSTimeInterval time;
//播放节
@property (copy  , nonatomic) NSString * playersectionname;


+(id)sharedOrder;

@end
