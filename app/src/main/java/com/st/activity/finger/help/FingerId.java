package com.st.activity.finger.help;

/**
 * List of finger IDs.
 */
public abstract class FingerId {
    
    /** No finger matched. */
    public static final int NONE = 0x00000000;

    /** Right thumb finger 右手大拇指. */
    public static final int RIGHT_THUMB = 0x00000001;

    /** Right index finger 右手食指. */
    public static final int RIGHT_INDEX = 0x00000002;

    /** Right middle finger 右手中指. */
    public static final int RIGHT_MIDDLE = 0x00000003;

    /** Right ring 右手无名指. */
    public static final int RIGHT_RING = 0x00000004;

    /** Right little 右小拇指. */
    public static final int RIGHT_LITTLE = 0x00000005;

    /** Left thumb finger 左手大拇指. */
    public static final int LEFT_THUMB = 0x00000006;

    /** Left index finger 左手食指. */
    public static final int LEFT_INDEX = 0x00000007;

    /** Left middle finger 左手中指. */
    public static final int LEFT_MIDDLE = 0x00000008;

    /** Left ring finger 左手无名指. */
    public static final int LEFT_RING = 0x00000009;

    /** Left little finger 左小拇指. */
    public static final int LEFT_LITTLE = 0x0000000a;
    
    /** 
     * String representations of finger names. 
     * For other purposes that sample this should be handled as <string-array> resource.
     */
    public static final String NAMES[] = {
        "无法识别", "右手大拇指", "右手食指", "右手中指", "右手无名指", "右手小拇指",
                "左手大拇指",  "左手食指",  "左手中指",  "左手无名指",  "左手小拇指"
    };

    /*public static final String NAMES[] = {
            "None", "Right Thumb", "Right Index", "Right Middle", "Right Ring", "Right Little",
            "Left Thumb",  "Left Index",  "Left Middle",  "Left Ring",  "Left Little"
    };*/
}
