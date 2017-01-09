package com.ryel.zaja.service;

import com.ryel.zaja.entity.Feedback;

/**
 * Created by billyu on 2017/1/9.
 */
public interface FeedbackService extends ICommonService<Feedback>{
    Feedback create(Feedback feedback);
}
