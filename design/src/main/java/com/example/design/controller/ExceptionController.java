package com.example.design.controller;


import com.example.design.VO.ResultVO;
import com.example.design.common.MyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MyException.class)
    public ResultVO handleValidException(MyException exception) {
        return ResultVO.error(exception.getCode(), exception.getMessage());
    }
    /**
     * 属性校验失败异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO handleValidException(MethodArgumentNotValidException exception) {
        StringBuilder strBuilder = new StringBuilder();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(e -> {
                    strBuilder.append(e.getField());
                    strBuilder.append(": ");
                    strBuilder.append(e.getDefaultMessage());
                    strBuilder.append("; ");
                });
        return ResultVO.error(400, strBuilder.toString());
    }
    /**
     * 重复插入数据
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResultVO handleValidException(DuplicateKeyException exception){
        return ResultVO.error(400,"数据已存在，插入或更新失败");
    }


    /**
     * 请求类型转换失败异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultVO handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        String msg = request.getRequestURI() +
                ": " + "请求地址参数" + exception.getValue() + "错误";
        return ResultVO.error(400, msg.toString());
    }

    /**
     * jackson json类型转换异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResultVO handleInvalidFormatException(InvalidFormatException exception) {
        StringBuilder strBuilder = new StringBuilder();
        exception.getPath()
                .forEach(p -> {
                    strBuilder.append("属性");
                    strBuilder.append(p.getFieldName());
                    strBuilder.append("，您输入的值：").append(exception.getValue());
                    strBuilder.append(", 类型错误！");
                });
        return ResultVO.error(400, strBuilder.toString());
    }

    /**
     * 方法级参数校验失败异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVO handleConstraintViolationException(ConstraintViolationException exception) {
        StringBuilder strBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        violations.forEach(v -> {
            strBuilder.append(v.getMessage()).append("; ");
        });
        return ResultVO.error(400, strBuilder.toString());
    }

//    @ExceptionHandler(Exception.class)
//    public ResultVO handleException(Exception exception) {
//        return ResultVO.error(400, "请求错误");
//    }
}
