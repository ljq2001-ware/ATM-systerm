package com.ljq;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Random r = new Random();
    private Account loginAcc ;//记住登陆后的用户账户
    /*启动ATM系统，展示欢迎界面*/
    public void start(){
        while (true) {
            System.out.println("===欢迎您进入到了ATM系统===");
            System.out.println("1.用户登录");
            System.out.println("2.用户开户");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch(command)
            {
                case 1:
                    //用户登录
                    login();
                    break;
                case 2:
                    //用户开户
                    createAccount();
                    break;
                default:
                    System.out.println("没有该操作~~");
                    break;
            }
        }
    }
    //用户登录操作
    private  void login(){
        System.out.println("==系统登录==");
        //1.判断系统中是否存在账户对象 存在登录 不存在结束
        if(accounts.size() == 0) {
            System.out.println("当前系统无任何账户，请先开户再来登录~~");
            return ;
        }
        //2.系统中存在账户对象
        while (true) {
            System.out.println("请您输入您的登陆卡号：");
            String cardId = sc.next();
            //判断卡号是否存在
            Account acc = getAccountByCardId(cardId);
            if(acc == null ){
                System.out.println("您输入的登录卡号不存在，请确认~~");
            }
            else{
                while (true) {
                    //卡号存在输入密码
                    System.out.println("请您输入登陆密码：");
                    String passWord = sc.next();
                    //判断密码是否正确
                    if(acc.getPassWord().equals(passWord))
                    {
                        loginAcc = acc;
                        //密码正确
                        System.out.println("恭喜您，" + acc.getUserName() + "登录成功,您的卡号是：" + acc.getCardId());
                        //展示登录后的操作界面
                        showUserCommand();
                        return ;//跳出并结束当前登录方法
                    }
                    else {
                        System.out.println("您输入的密码不正确，请确认~~");
                    }
                }
            }
        }
    }
    //登陆后的操作界面
    private void showUserCommand(){
        while (true) {
            System.out.println(loginAcc.getUserName() + "您可以选择如下功能进行账户处理====");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.密码修改");
            System.out.println("6.退出");
            System.out.println("7.注销当前账户");
            System.out.println("请选择:");
            int command = sc.nextInt();
            switch (command)
            {
                case 1:
                    //查询当前账户
                    showLoginAccount();
                    break;
                case 2:
                    //存款
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    updatePassword();
                    return ;//跳出并结束当前方法
                case 6:
                    System.out.println(loginAcc.getUserName() + "您退出系统成功!");
                    return ;
                case 7:
                    //销户
                    if (deleteAccount()) {
                        //成功 回到欢迎页面
                        return;
                    }
                    break;
                default:
                    System.out.println("您当前选择的操作是不存在的，请确认~~~");
                    break;
            }
        }
    }

    private void updatePassword() {
        System.out.println("==账户密码修改==");
        while (true) {
            System.out.println("请您输入当前账户的密码：");
            String passWord = sc.next();
            if(loginAcc.getPassWord().equals(passWord))
            {
                //修改密码
                while (true) {
                    System.out.println("请您输入新密码：");
                    String newPassword = sc.next();
                    System.out.println("请您再次输入密码：");
                    String okPassword = sc.next();
                    if(newPassword.equals(okPassword))
                    {
                        loginAcc.setPassWord(okPassword);
                        System.out.println("修改成功~");
                        return;
                    }
                    else
                    {
                        System.out.println("你输入的两次密码不一致");
                    }
                }
            }
            else{
                System.out.println("您当前输入的密码有误~");
            }
        }
    }

    //销户
    private boolean deleteAccount() {
        System.out.println("==进行销户操作==");
        System.out.println("请问您确认销户吗？y/n");
        String command = sc.next();
        switch (command)
        {
            case "y":
                //判断有没有钱
                if(loginAcc.getMoney() == 0)
                {
                    accounts.remove(loginAcc);
                    System.out.println("销户成功！");
                    return true;
                }
                else
                {
                    System.out.println("对不起，您的帐户存在金额，不允许销户！！");
                    return false;
                }
            default:
                System.out.println("好的，您的账户保留！！");
                return false;
        }
    }

    //转账
    private void transferMoney() {
        //判断系统中是否存在其他账户
        if(accounts.size() < 2){
            System.out.println("当前系统中只有您一个账户，无法为其他账户转账！");
            return;
        }
        //判断自己的账户是否有钱
        if(loginAcc.getMoney() == 0 )
        {
            System.out.println("您自己都没钱，就别转了~~~");
            return;
        }
        //开始转账
        while (true) {
            System.out.println("请您输入对方的卡号：");
            String cardId = sc.next();
            //判断卡号是否正确
            Account acc  = getAccountByCardId(cardId);
            if(acc == null){
                System.out.println("您输入的对方卡号有误~~");
            }
            else {
                //继续认证姓氏
                String name ="*" +  acc.getUserName().substring(1);
                System.out.println("请您输入【" + name + "】的姓氏：");
                String preName = sc.next();
                //判断姓氏是否正确
                if(acc.getUserName().startsWith(preName)){
                    //认证通过
                    while (true) {
                        System.out.println("请您输入转账给对方的金额：");
                        double money = sc.nextDouble();
                        //判断金额是否没有超过自己的余额
                        if(loginAcc.getMoney() >= money){
                            //转给对方
                            //更新自己的
                            loginAcc.setMoney(loginAcc.getMoney() - money);
                            //更新对方的
                            acc.setMoney(acc.getMoney() + money);
                            System.out.println("您转账成功啦~~");
                            return;//直接跳出转载方法
                        }
                        else {
                            System.out.println("您余额不足，无法给对方转这么多钱，最多可以转：" + loginAcc.getMoney());
                        }
                    }
                }
                else
                {
                    System.out.println("对不起，您认证的姓氏有问题~~~");
                }
            }
        }
    }

    //取钱
    private void drawMoney() {
        System.out.println("==取钱操作==");
        //先判断账户余额是否大于100 不够100不能取
        if(loginAcc.getMoney() < 100 )
        {
            System.out.println("您的帐户余额不足100元，不允许取钱~~~");
            return ;
        }
        else{
            while (true) {
                System.out.println("请您输入取款金额：");
                double money = sc.nextDouble();
                //判断账户余额是否够
                if(loginAcc.getMoney() >= money)
                {
                    //取款金额不能超过当此限额
                    if(money > loginAcc.getLimit())
                    {
                        System.out.println("您当前取款金额超过了每次限额，您每次最多可以取：" + loginAcc.getLimit());
                    }
                    else {
                        //开始取钱
                        loginAcc.setMoney(loginAcc.getMoney() - money);
                        System.out.println("您取款：" + money + "成功，取款后剩余：" + loginAcc.getMoney());
                        break;
                    }
                }
                else{
                    System.out.println("余额不足，您的帐户余额是：" + loginAcc.getMoney());
                }
            }
        }
    }


    //存款
    private void depositMoney() {
        System.out.println("==存钱操作==");
        System.out.println("请您输入存款金额：");
        double money = sc.nextDouble();
        //更新当前账户余额
        loginAcc.setMoney(loginAcc.getMoney() + money);
        System.out.println("恭喜您，您存钱:" + money +"成功，目前余额为:" + loginAcc.getMoney());
    }


    //展示当前登录信息
    private void showLoginAccount(){
        System.out.println("==当前您的账户信息如下：==");
        System.out.println("卡号：" + loginAcc.getCardId());
        System.out.println("户主：" + loginAcc.getUserName());
        System.out.println("户主性别：" + loginAcc.getSex());
        System.out.println("余额：" + loginAcc.getMoney());
        System.out.println("每次取现额度：" + loginAcc.getLimit());
    }

    //用户开户操作
    private void createAccount(){
        System.out.println("==系统开户操作==");
        //1.创建一个账户对象用于封装用户的开户信息
        Account acc = new Account();
        //2.需要用户输入自己的开户信息，赋值给账户对象
        System.out.println("请您输入您的账户名称:");
        String name = sc.next();
        acc.setUserName(name);

        //charAt 返回字符串中指定索引位置的值
        while (true) {
            System.out.println("请您输入您的性别:");
            char sex = sc.next().charAt(0);
            if(sex == '男' || sex == '女') {
                acc.setSex(sex);
                break;
            }
            else System.out.println("您输入的性别有误~只能是男或者女");
        }

        while (true) {
            System.out.println("请您输入您的账号密码：");
            String passWord = sc.next();
            System.out.println("请您输入您的确认密码：");
            String okPassWord = sc.next();
            //判断两次密码是否一样
            if(okPassWord.equals(passWord))
            {
                acc.setPassWord(passWord);
                break;
            }
            else System.out.println("您输入的两次密码不一致，请您确认~~");
        }

        System.out.println("请您输入您的取现额度:");
        double limit = sc.nextInt();
        acc.setLimit(limit);

        //为账户生成一个卡号,(由系统自动生成，8位数字表示 不能与其他账户卡号重复)
        String newCardId = createCardId();
        acc.setCardId(newCardId);


        //3.把账户对象存入到账户集合中
        accounts.add(acc);
        System.out.println("恭喜您，" + acc.getUserName() + "开户成功,您的卡号是：" + acc.getCardId());
    }

    //返回一个8位数字的卡号，不能与其他账户卡号重复
    private String createCardId(){
        while (true) {
            String cardId = "";
            //使用循环 产生卡号
            for (int i = 0; i < 8; i++) {
               int data = r.nextInt(10); //0-9
                cardId += data;
            }
            //判断cardId是否重复,没有重复再返回
            Account acc =getAccountByCardId(cardId);
            if(acc == null){
                //说明没有找到
                return cardId;
            }
        }
    }
    //根据卡号查询账户对象 返回
    private Account getAccountByCardId(String cardId){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if(acc.getCardId().equals(cardId)) {
                return acc;
            }
        }
        return null;//查无此账户
    }


}
