package com.yunlbd.syncdata.service;

/**
 * @author Wangts
 * @Project_Name dailyreport
 * @date 2023年12月18日 13:25
 */
public interface IMailService {

    /**
     * 发送文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath);

    /**
     * 发送模板邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param fileName 邮件模板文件名称
     * @param model    邮件数据载体
     */
    void sendModelMail(String to, String subject, String fileName, Object model);

}