//
//  SectionCell.m
//  mLearning3.19
//
//  Created by zhijin on 14-3-20.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import "SectionCell.h"

@implementation SectionCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        UIImageView * imageV =[[UIImageView alloc]initWithFrame:CGRectMake(10*2, 10*2, 100*2, 75*2)];
        imageV.image = [UIImage imageNamed:@"课程图片.png"];
        
        self.sectionlabel = [[UILabel alloc]initWithFrame:CGRectMake(125*2, 10*2, 150*2, 18*2)];
        self.sectionlabel.backgroundColor = [UIColor clearColor];
        self.sectionlabel.textColor = [UIColor grayColor];
        self.sectionlabel.textAlignment = NSTextAlignmentCenter;
        
        self.label = [[UILabel alloc]initWithFrame:CGRectMake(125*2, 25*2, 150*2, 50*2)];
        self.label.backgroundColor = [UIColor clearColor];
        self.label.textColor = [UIColor lightGrayColor];
        self.label.font = [UIFont systemFontOfSize:13];
        self.label.lineBreakMode = NSLineBreakByTruncatingTail;
        self.label.numberOfLines = 2;
        
        self.timelabel = [[UILabel alloc]initWithFrame:CGRectMake(125*2, 73*2, 150*2, 13*2)];
        self.timelabel.backgroundColor = [UIColor clearColor];
        self.timelabel.textColor = [UIColor lightGrayColor];
        
        UIButton * btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(280*2, 27*2, 50*2, 50*2);
        [btn setBackgroundImage:[UIImage imageNamed:@"more.png"] forState:UIControlStateNormal];
        
        UIView *aView = [[UIView alloc] initWithFrame:self.contentView.frame];
        aView.backgroundColor = [UIColor grayColor];
        self.selectedBackgroundView = aView;
        
        [self addSubview:imageV];
        [self addSubview:self.sectionlabel];
        [self addSubview:self.label];
        [self addSubview:self.timelabel];
        [self addSubview:btn];
        
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
