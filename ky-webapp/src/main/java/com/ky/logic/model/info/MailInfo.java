package com.ky.logic.model.info;

import com.ky.logic.utils.FileUtil;
import org.apache.commons.mail.EmailAttachment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 邮件信息<br>
 * Created by yl on 2017/8/25.
 */
public class MailInfo {

    // 收件人
    private List<String> toAddress = new ArrayList<String>();

    // 抄送人地址
    private List<String> ccAddress = new ArrayList<String>();

    // 密送人
    private List<String> bccAddress = new ArrayList<String>();

    // 附件信息
    private List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();

    // File 类型附件
    private List<File> attachFile = new ArrayList<>();

    // 邮件主题
    private String subject;

    // 邮件的文本内容
    private String content;

    // vm 模板参数
    private Map<String, Object> vm;


    /**
     * 发送人昵称
     */
    private String senderNick;

    /**
     * 发送邮箱服务器
     */
    private String smtpServer;

    /**
     * 邮箱服务器端口
     */
    private String smtpPort;


    /**
     * 发送人邮箱
     */
    private String senderAddress;

    /**
     * 发送人密码是否加密 默认 true
     */
    private boolean isAES = true;

    /**
     * 发送人密码
     */
    private String senderPassword;

    // 是否需要身份验证
    private boolean validate = false;


    // 追加接收人
    public void addTo(String toAddress) {
        this.toAddress.add(toAddress);
    }

    // 追加接收人列表
    public void addTo(List<String> toAddress) {
        this.toAddress.addAll(toAddress);
    }

    // 追加抄送人列表
    public void addCc(List<String> ccAddress) {
        if (null != ccAddress && ccAddress.size() > 0)
            this.ccAddress.addAll(ccAddress);
    }


    // 追加附件
    public void addAtt(EmailAttachment att) {
        this.attachments.add(att);
    }

    // 追加附件列表
    public void addAtt(List<EmailAttachment> att) {
        if (null != att && att.size() > 0) {
            this.attachments.addAll(att);
        }

    }

    public void addAtt(String filePath) {
        // 创建附件
        EmailAttachment att = new EmailAttachment();
        att.setPath(filePath);                             //本地附件，绝对路径
        att.setName(FileUtil.getFileName(filePath));       //附件名称
        att.setDisposition(EmailAttachment.ATTACHMENT);
        //att.setDescription();                            //附件描述

        addAtt(att);
    }


    public List<EmailAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Map<String, Object> getVm() {
        return vm;
    }

    public void setVm(Map<String, Object> vm) {
        this.vm = vm;
    }


    public List<String> getToAddress() {
        return toAddress;
    }

    public void setToAddress(List<String> toAddress) {
        this.toAddress = toAddress;
    }

    public List<String> getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(List<String> ccAddress) {
        this.ccAddress = ccAddress;
    }

    public List<String> getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(List<String> bccAddress) {
        this.bccAddress = bccAddress;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSenderNick() {
        return senderNick;
    }

    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public boolean isAES() {
        return isAES;
    }

    public void setAES(boolean isAES) {
        this.isAES = isAES;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }


    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }


    public List<File> getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(List<File> attachFile) {
        this.attachFile = attachFile;
    }

    @Override
    public String toString() {
        String att = attachments.size() > 0 ? attachments.get(0).getPath() : "";
        return "[subject=" + subject + ", toAddress=" + toAddress + ", attachments=" + att
                + ", content=" + content + "]";
    }
}
