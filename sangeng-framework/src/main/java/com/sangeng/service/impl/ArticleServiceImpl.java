package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.ArticleDetailVo;
import com.sangeng.domain.vo.ArticleListVo;
import com.sangeng.domain.vo.HotArticleVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;

import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-08-16 15:05:56
 */
@Slf4j
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    CategoryService categoryService;

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
        ArrayList<HotArticleVo> hotArticleVos = new ArrayList<>();
        for (Article record : records) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(record,vo);
            hotArticleVos.add(vo);
        }
        log.info("查询结束");
        return ResponseResult.okResult(hotArticleVos);

    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        log.info("进入 articleList服务");
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName
//        articles.stream()
//                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
//                .collect(Collectors.toList());
        //articleId去查询articleName进行设置
        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());}

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        log.info("退出 articleList服务");
        return ResponseResult.okResult(pageVo);


    }

    @Override
    public ResponseResult getArticleDetail(Long id) {

        log.info("进入ArticleDetail服务");
        //根据id查询文章
        Article article = getById(id);
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        String categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        log.info("退出ArticleDetail服务");
        //封装响应返回

        return ResponseResult.okResult(articleDetailVo);
    }
}

