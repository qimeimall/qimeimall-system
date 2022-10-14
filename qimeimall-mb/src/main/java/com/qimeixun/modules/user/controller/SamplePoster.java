package com.qimeixun.modules.user.controller;

import com.quaint.poster.annotation.PosterBackground;
import com.quaint.poster.annotation.PosterFontCss;
import com.quaint.poster.annotation.PosterImageCss;
import com.quaint.poster.core.abst.AbstractDefaultPoster;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author chenshouyang
 * @date 2022/9/319:17
 */
@Data
@Builder
public class SamplePoster extends AbstractDefaultPoster {


    /**
     * 背景图
     */
    @PosterBackground(width = 658,height = 1256)
    private BufferedImage backgroundImage;
    /**
     * 名称
     */
    @PosterFontCss( position = { 280, 1080}, size= 30, canNewLine = {1 , 300, 5})
    private String nickName;


    /**
     * 二维码图
     */
    @PosterImageCss(position = {100,1040},width = 168,height = 168)
    private BufferedImage mainImage;

    @Tolerate
    public SamplePoster() {}
}
