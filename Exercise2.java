package com.github.jwxa;

/**
 * 类描述
 * <p>
 * 方法描述列表
 * </p>
 * User: Jwxa Date: 2018/8/23 ProjectName: coding-myself Version: 1.0
 */
public class Exercise2 {


  public static void main(String[] args) throws Exception {
    MessageFactory factory = MessageFactoryBean.getInstance();
    Message message = factory.newMessage("110");
    message.printMessage();
  }


}

