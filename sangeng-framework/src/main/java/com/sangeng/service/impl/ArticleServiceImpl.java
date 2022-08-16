package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-08-16 15:05:56
 */
@Slf4j
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public ResponseResult hotArticleList() {
        log.info("开始进入数据库查询");

        //查询热门文章
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus,0);
        wrapper.orderByAsc(Article::getViewCount);
        Page<Article> page = new Page(1,10);
        page(page,wrapper);
        List<Article> records = page.getRecords();
        log.info("查询结束");
        return ResponseResult.okResult(records);

    }
}

