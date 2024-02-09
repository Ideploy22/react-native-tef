
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNIdeployTefSpec.h"

@interface IdeployTef : NSObject <NativeIdeployTefSpec>
#else
#import <React/RCTBridgeModule.h>

@interface IdeployTef : NSObject <RCTBridgeModule>
#endif

@end
