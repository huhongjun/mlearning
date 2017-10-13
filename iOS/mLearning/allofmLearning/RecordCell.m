//
//  RecordCell.m
//  mLearning3.19
//
//  Created by zhijin on 14-3-21.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import "RecordCell.h"

@implementation RecordCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        self.name = [[UILabel alloc]initWithFrame:CGRectMake(20*2, 0, 160*2, 44*2)];
        self.name.backgroundColor = [UIColor clearColor];
        self.name.font = [UIFont systemFontOfSize:13];
        self.name.textColor = [UIColor lightGrayColor];

        
        self.time = [[UILabel alloc]initWithFrame:CGRectMake(180*2, 0, 160*2, 44*2)];
        self.time.backgroundColor = [UIColor clearColor];
        self.time.font = [UIFont systemFontOfSize:13];
        self.time.textColor = [UIColor lightGrayColor];

        UIView *aView = [[UIView alloc] initWithFrame:self.contentView.frame];
        aView.backgroundColor = [UIColor grayColor];
        self.selectedBackgroundView = aView;
        
        [self addSubview:self.name];
        [self addSubview:self.time];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
