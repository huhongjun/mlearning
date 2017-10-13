//
//  LearningRecordViewController.h
//  mLearning
//
//  Created by zhijin on 14-3-31.
//  Copyright (c) 2014年 知金教育. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MediaPlayer/MediaPlayer.h>
#import <AVFoundation/AVAudioSession.h>
#import "ASIHTTPRequest.h"
#import "Reachability.h"
#import "ASIFormDataRequest.h"
#import "RecordCell.h"
#import "Ton.h"
#import "JSONKit.h"

@interface LearningRecordViewController : UIViewController<UITableViewDataSource,UITableViewDelegate>

{
    MPMoviePlayerViewController * moviePlayer;
    ASIHTTPRequest * myrequest;
    ASIFormDataRequest * arequest;
}

@property (strong , nonatomic)MPMoviePlayerViewController * moviePlayer;
@property (retain, nonatomic) ASIHTTPRequest * myrequest;
@property (retain, nonatomic) ASIFormDataRequest * arequest;

@end
