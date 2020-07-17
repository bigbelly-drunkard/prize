package org.egg.utils;


import org.egg.model.DTO.EmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * @author cdt
 * @Description 邮件
 * @date: 2018/8/10 18:31
 */
public class EmailUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

    private static ThreadPoolTaskExecutor poolExecutorSendEmail;
    private static final String FORMACC = "599189309@qq.com";
    public static final String TOEMAILACC = "1439129295@qq.com";
    private static final String FORMACCPWD = "不可用";

    static {
        poolExecutorSendEmail = new ThreadPoolTaskExecutor();
        poolExecutorSendEmail.setCorePoolSize(5);
        poolExecutorSendEmail.setMaxPoolSize(20);
        poolExecutorSendEmail.setKeepAliveSeconds(200);
        poolExecutorSendEmail.setQueueCapacity(100);
        poolExecutorSendEmail.initialize();
    }


    /**
     * 发送简单的文本邮件
     */
    public static void sendEmailFormQQ(EmailDto emailDto) {
        return;
//        poolExecutorSendEmail.execute(() -> {
//            Thread.currentThread().setName("sendEmailFormQQ" + DateUtil.format(new Date(), DateUtil.YMDHMSSS));
//            LOGGER.info("sendEmailFormQQ  start,emailDto={}", JSONObject.toJSONString(emailDto));
//
//            try {
//                Session session = getSession();
//                MimeMessage simpleMail = createSimpleMail(session, emailDto.getToEmailAcc(), emailDto.getTitle(), emailDto.getContent());
//                send(session, simpleMail);
//            } catch (Exception e) {
//                LOGGER.error("sendEmailFormQQ Exception e={}", e);
//            }
//
//        });
    }

    private static Session getSession() {
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com");
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
//        prop.put("mail.smtp.port", "25");
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");

        prop.setProperty("mail.smtp.socketFactory.port", "465");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        return session;
    }

    private static void send(Session session, Message message) throws Exception {
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        try {
            //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
            ts.connect("smtp.qq.com", FORMACC, FORMACCPWD);
            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());
        } finally {
            ts.close();
        }
    }

    /**
     * @param session
     * @return
     * @throws Exception
     * @Method: createSimpleMail
     * @Description: 创建一封只包含文本的邮件
     * @Anthor:
     */
    private static MimeMessage createSimpleMail(Session session, String toEmailAcc, String title, String content)
            throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(FORMACC));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAcc));
        //邮件的标题
        message.setSubject(title);
        //邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }

    /**
     * @param session
     * @return
     * @throws Exception
     * @Method: createImageMail
     * @Description: 生成一封邮件正文带图片的邮件
     * @Anthor:
     */
    private static MimeMessage createImageMail(Session session, String toEmailAcc, String title, String content, String imgUrl, String imgId) throws Exception {
        //创建邮件
        MimeMessage message = new MimeMessage(session);
        // 设置邮件的基本信息
        //发件人
        message.setFrom(new InternetAddress(FORMACC));
        //收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAcc));
        //邮件标题
        message.setSubject(title);

        // 准备邮件数据
        // 准备邮件正文数据
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(content, "text/html;charset=UTF-8");
        // 准备图片数据
        MimeBodyPart image = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource(imgUrl));
        image.setDataHandler(dh);
        image.setContentID(imgId);
        // 描述数据关系
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.addBodyPart(image);
        mm.setSubType("related");

        message.setContent(mm);
        message.saveChanges();
        //将创建好的邮件写入到E盘以文件的形式进行保存
//        message.writeTo(new FileOutputStream("E:\\ImageMail.eml"));
        //返回创建好的邮件
        return message;
    }

    /**
     * @param session
     * @return
     * @throws Exception
     * @Method: createAttachMail
     * @Description: 创建一封带附件的邮件
     * @Anthor:
     */
    private static MimeMessage createAttachMail(Session session) throws Exception {
        MimeMessage message = new MimeMessage(session);

        //设置邮件的基本信息
        //发件人
        message.setFrom(new InternetAddress("gacl@sohu.com"));
        //收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("xdp_gacl@sina.cn"));
        //邮件标题
        message.setSubject("JavaMail邮件发送测试");

        //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("使用JavaMail创建的带附件的邮件", "text/html;charset=UTF-8");

        //创建邮件附件
        MimeBodyPart attach = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("src\\2.jpg"));
        attach.setDataHandler(dh);
        attach.setFileName(dh.getName());  //

        //创建容器描述数据关系
        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(text);
        mp.addBodyPart(attach);
        mp.setSubType("mixed");

        message.setContent(mp);
        message.saveChanges();
        //将创建的Email写入到E盘存储
//        message.writeTo(new FileOutputStream("E:\\attachMail.eml"));
        //返回生成的邮件
        return message;
    }

    /**
     * @param session
     * @return
     * @throws Exception
     * @Method: createMixedMail
     * @Description: 生成一封带附件和带图片的邮件
     * @Anthor:
     */
    private static MimeMessage createMixedMail(Session session) throws Exception {
        //创建邮件
        MimeMessage message = new MimeMessage(session);

        //设置邮件的基本信息
        message.setFrom(new InternetAddress("gacl@sohu.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("xdp_gacl@sina.cn"));
        message.setSubject("带附件和带图片的的邮件");

        //正文
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("xxx这是女的xxxx<br/><img src='cid:aaa.jpg'>", "text/html;charset=UTF-8");

        //图片
        MimeBodyPart image = new MimeBodyPart();
        image.setDataHandler(new DataHandler(new FileDataSource("src\\3.jpg")));
        image.setContentID("aaa.jpg");

        //附件1
        MimeBodyPart attach = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("src\\4.zip"));
        attach.setDataHandler(dh);
        attach.setFileName(dh.getName());

        //附件2
        MimeBodyPart attach2 = new MimeBodyPart();
        DataHandler dh2 = new DataHandler(new FileDataSource("src\\波子.zip"));
        attach2.setDataHandler(dh2);
        attach2.setFileName(MimeUtility.encodeText(dh2.getName()));

        //描述关系:正文和图片
        MimeMultipart mp1 = new MimeMultipart();
        mp1.addBodyPart(text);
        mp1.addBodyPart(image);
        mp1.setSubType("related");

        //描述关系:正文和附件
        MimeMultipart mp2 = new MimeMultipart();
        mp2.addBodyPart(attach);
        mp2.addBodyPart(attach2);

        //代表正文的bodypart
        MimeBodyPart content = new MimeBodyPart();
        content.setContent(mp1);
        mp2.addBodyPart(content);
        mp2.setSubType("mixed");

        message.setContent(mp2);
        message.saveChanges();

//        message.writeTo(new FileOutputStream("E:\\MixedMail.eml"));
        //返回创建好的的邮件
        return message;
    }
}
