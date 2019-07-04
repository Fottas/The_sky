package com.yhau.controller;

import com.yhau.core.util.StaticUtil;
import com.yhau.model.Question;
import com.yhau.model.ViewObject;
import com.yhau.service.FollowService;
import com.yhau.service.QuestionService;
import com.yhau.service.SearchService;
import com.yhau.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search(Model model, @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "10") int count) {
        try {
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count, "<em>", "</em>");
            List<ViewObject> vos = new ArrayList<>();

            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionService.selectById(questionList.get(i).getId());
                ViewObject vo = new ViewObject();
                if (question.getContent() != null) {
                    question.setContent(questionList.get(i).getContent());
                }
                if (question.getTitle() != null) {
                    question.setTitle(questionList.get(i).getTitle());
                }
                vo.set("question", question);
                vo.set("followCount", followService.getFollowerCount(StaticUtil.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUser(question.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            logger.error("搜索评论异常" + e.getMessage());
        }
        return "/result.html";
    }
}
