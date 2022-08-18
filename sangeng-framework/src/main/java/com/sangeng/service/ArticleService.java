package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-08-16 15:01:39
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId);

    ResponseResult getArticleDetail(Long id);
}

