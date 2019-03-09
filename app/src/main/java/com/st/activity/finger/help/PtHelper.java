package com.st.activity.finger.help;

import com.upek.android.ptapi.PtConstants;

public class PtHelper
{
	/** 
     * Create message for given GUI callback message
     */
	public static final String GetGuiStateCallbackMessage(int guiState, int message, byte progress)
	{
		String s = null;
		
		if((guiState & PtConstants.PT_MESSAGE_PROVIDED) != PtConstants.PT_MESSAGE_PROVIDED)
        {
			return s;
        }
        switch(message)
        {
        // Generic GUI messages:            
        case PtConstants.PT_GUIMSG_PUT_FINGER:
        case PtConstants.PT_GUIMSG_PUT_FINGER2:
        case PtConstants.PT_GUIMSG_PUT_FINGER3:
        case PtConstants.PT_GUIMSG_PUT_FINGER4:
        case PtConstants.PT_GUIMSG_PUT_FINGER5:
//            s = "Put Finger";
            s = "请将手指放置指纹识别器！";
            break;
        case PtConstants.PT_GUIMSG_KEEP_FINGER:
//            s = "Keep Finger";
            s = "请保持手指不动！";
            break;
        case PtConstants.PT_GUIMSG_REMOVE_FINGER:
//            s = "Lift Finger";
            s = "请拿开手指！";
            break;
        case PtConstants.PT_GUIMSG_BAD_QUALITY:
        case PtConstants.PT_GUIMSG_TOO_STRANGE:
//            s = "Bad Quality";
            s = "无法识别当前手指的指纹信息！";
            break;
        case PtConstants.PT_GUIMSG_TOO_LEFT:
//            s = "Too Left";
            s = "手指太靠左，请移动手指！";
            break;
        case PtConstants.PT_GUIMSG_TOO_RIGHT:
//            s = "Too Right";
            s = "手指太靠右，请移动手指！";
            break;
        case PtConstants.PT_GUIMSG_TOO_LIGHT:
//            s = "Too Light";
            s = "请将手指紧靠指纹识别器！";
            break;
        case PtConstants.PT_GUIMSG_TOO_DRY:
//            s = "Too Dry";
            s = "请保持手指干燥！";
            break;
        case PtConstants.PT_GUIMSG_TOO_SMALL:
//            s = "Too Small";
            s = "请将手指紧靠指纹识别器！";
            break;
        case PtConstants.PT_GUIMSG_TOO_SHORT:
//            s = "Too Short";
            s = "手指放置时间过短！";
            break;
        case PtConstants.PT_GUIMSG_TOO_HIGH:
//            s = "Too High";
            s = "手指放置位置过高！";
            break;
        case PtConstants.PT_GUIMSG_TOO_LOW:
//            s = "Too Low";
            s = "手指放置位置过低！";
            break;
        case PtConstants.PT_GUIMSG_TOO_FAST:
//            s = "Too Fast";
            s = "手指移动过于频繁！";
            break;
        case PtConstants.PT_GUIMSG_TOO_SKEWED:
//            s = "Too Skewed";
            s = "手指放置位置倾斜！";
            break;
        case PtConstants.PT_GUIMSG_TOO_DARK:
//            s = "Too Dark";
            s = "请将手指紧靠指纹识别器！";
            break;
        case PtConstants.PT_GUIMSG_BACKWARD_MOVEMENT:
//            s = "Backward movement";
            s = "请将手指向后移动！";
            break;
        case PtConstants.PT_GUIMSG_JOINT_DETECTED:
//            s = "Joint Detectected";
            s = "请将手指紧靠指纹识别器！";
            break;
        case PtConstants.PT_GUIMSG_CENTER_AND_PRESS_HARDER:
//            s = "Press Center and Harder";
            s = "请将手指紧靠指纹识别器！";
            break;
        case PtConstants.PT_GUIMSG_PROCESSING_IMAGE:
//            s = "Processing Image";
            s = "请将手指紧靠指纹识别器！";
            break;
        // Enrollment specific:
        case PtConstants.PT_GUIMSG_NO_MATCH:
//            s = "Template extracted from last swipe doesn't match previous one";
            s = "上次刷出来的指纹信息与前一个不匹配！";
            break;
        case PtConstants.PT_GUIMSG_ENROLL_PROGRESS:
//            s = "Enrollment progress";
            s = "进度";
            if((guiState & PtConstants.PT_PROGRESS_PROVIDED) == PtConstants.PT_PROGRESS_PROVIDED)
            {
                s += ": " + progress + '%';
            }

        default:
            break;
        }
        
        return s;
	}

}
