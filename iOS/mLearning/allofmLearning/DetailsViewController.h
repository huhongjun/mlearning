//
//  DetailsViewController.h
//  mLearning
//
//  Created by zhijin on 14-3-31.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MediaPlayer/MediaPlayer.h>
#import <AVFoundation/AVAudioSession.h>
#import "ASIFormDataRequest.h"
#import "Reachability.h"
#import "Ton.h"
#import "JSONKit.h"

@interface DetailsViewController : UIViewController

{
    MPMoviePlayerViewController * moviePlayer;
    ASIFormDataRequest * arequest;
}

@property (strong , nonatomic)MPMoviePlayerViewController * moviePlayer;
@property (retain, nonatomic) UILabel * sectionlabel;
@property (retain, nonatomic) UILabel * label;
@property (retain, nonatomic) UILabel * timelabel;
@property (retain, nonatomic) ASIFormDataRequest * arequest;

@end
