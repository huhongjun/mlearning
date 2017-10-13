//
//  Ton.m
//  mLearning
//
//  Created by zhijin on 14-3-28.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import "Ton.h"

static Ton *ton=nil;

@implementation Ton

+(id)sharedOrder
{
    if (ton==nil)
    {
        ton=[[Ton alloc]init];
        ton.SectionArray = [[NSMutableArray alloc]initWithCapacity:10];
    }
    return ton;
}

@end
