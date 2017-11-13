# example
Activity+Fragment典型app框架

Activity页面使用
LaunchActivity：启动页面，加载用户数据，更新检测等操作在此界面实现，操作结束后根据实际业务决定跳转GuideActivity、HomePageActivity或者DetailActivity。
GuideActivity：首次启动的多图引导页，内含一个ViewPager，图片根据需求增删，最后一个页面根据业务跳转HomePageActivity或者DetailActivity。
HomePageActivity：主界面，页面数量由底部导航功能块决定，改页面展示界面一定包含底部导航栏，任意功能块内二次跳转均使用DetailActivity展示处理。
DetailActivity：内容管理核心展示页面，展示内容由单个Fragment实现后嵌套该页面。

Fragment页面创建及使用
任何交互界面原则上均以Fragment创建并实现，然后嵌套入DetailActivity界面展示。
创建：
一、	创建位置以包名归类，从主页延伸的页面必须创建在
com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.home下，设置相关页面必须创建在com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.mysettings下，以此类推。
二、命名规范：
1、	以“业务名称+Fragment”命名，如注册页面：RegisterFragment
2、	所使用布局文件以“业务名称_fragment_layout”格式命名，如注册页面使用布局：register_fragment_layout
三、必须继承自BaseFragment，以确保返回键的监听以及页面跳转管理
四、	使用规范：
1、为保证内存开销，原则上不允许直接持有fragment对象，关于跳转和数据传递按下面方法也无需创建fragment实例对象。若一定需要获取某个fragmnent实例对象，必须使用FragmentMapManager单例类获取，全局禁止单独new出fragment的实例对象。
以新建RegisterFragment后为例：
获取对象方法：
FragmentMapManager fmm = FragmentMapManager.getInstance();
Fragment fragment = fmm.getFragment(RegisterFragment.class)或
Fragment fragment = fmm.getFragment(RegisterFragment.class.getName())；
2、	页面的跳转：
使用setTransactionFragment(Class<?> cls)或
setTransactionFragment(String classname)直接跳转指定页面
以新建RegisterFragment后为例：
setTransactionFragment(RegisterFragment.class)或
setTransactionFragment(RegisterFragment.class.getName())
3、	Fragment间数据的传递，传递对象为Intent，调用页面跳转方法的参数中直接增加Intent对象即可：
以新建RegisterFragment后为例：
Intent intent = new Intent();
可选：
Bundle bundle = new Bundle();
bundle.put...
...
intent.putExtras(bundle)
		intent.put...
...
setTransactionFragment(RegisterFragment.class, intent)或
setTransactionFragment(RegisterFragment.class.getName(),intent)
然后在需要接受接收数据的Fragment中实现父类onIntent(Intent intent)方法，接收Intent对象。
4、	从其他Activity中跳转，与上述二、三相同，方法名更改为BaseActivity中的setDetailsFragment()方法。
5、	back键的处理，必须实现BaseFragment内的抽象方法onBackPressed()，在该方法内处理是否对系统back键的截断，若有需要可更改为return true则可拦截系统back键。处理完事务后务必确保return false。否则back失效。

用户管理中心 UserInfo.java
类：com.dx_tech.wnlbs.smartcommunity.owner.utils.UserInfo.java
对象获取：UserInfo userInfo = UserInfo.getInstance(context)
属性获取：userInfo.XX
属性更新：userInfo.XX = YY;
		  UserInfo.getInstance(context).upDataUserInfo();
第三方框架
线程通信：EventBus3.0
Http客户端：retrofit2.0 建议搭配GsonFormat插件使用
网络请求：OkHttp3
View注入：Butter Knife

以上为目前已加入并使用的第三方框架，建议在开发之初对不熟悉的先做了解，开发过程中尽量按照框架结构开发可很大程度提高代码质量。
服务器接口创建规则
所有与服务器通讯接口需按照以下步骤添加后使用：
以Example名接口为例
1、	在com.dx_tech.wnlbs.smartcommunity.owner.constant.HttpConstant.java中添加声明接口名称及通讯地址
2、	在com.dx_tech.wnlbs.smartcommunity.owner.bean包中创建接收数据类型类
命名规范：接口名+Bean
如：ExampleBean.java
根据服务器具体返回的数据实现接收数据的具体结构，建议使用GsonFormat插件快速生成
3、	在com.dx_tech.wnlbs.smartcommunity.owner.contract包中创建接口应用内接口
命名规范：接口名+Contract
如：ExampleContract.java
a.创建逻辑处理层接口：
命名规范：I+接口名+ Model
如：IExampleModel
接口内抽象回调方法
b.继承IbaseVie.java创建视图层接口：
命名规范：I+接口名+ View
如：IexampleView
接口内抽象对返回数据的处理方法
c.创建连接层接口
命名规范：I+接口名+ Presenter
接口内抽象对接口的调用方法
4、	在com.dx_tech.wnlbs.smartcommunity.owner.model包内继承刚刚创建的逻辑处理层接口创建接口实例化模型：
命名规范：接口名+ Model
如：ExampleModel.java
实现内部方法创建接口的实例化对象
5、	在com.dx_tech.wnlbs.smartcommunity.owner.presenter包内继承刚刚创建的连接层接口创建控制层控制体
命名规范：接口名+ Presenter
如：ExamplePresenter.java
于构造器中接收传递进来的视图对象，于方法体中完成视图与模型的耦合
6、	在com.dx_tech.wnlbs.smartcommunity.owner.api.ApiService.java类中以retrofit2框架格式增加接口通讯规则

服务器接口的使用
对需要使用接口的窗口（Fragment或者Activity）实现被调用接口的视图层接口接口从实现的方法中获取接口调用后的回调数据。通过接口控制层构造方法实例化接口对象，调用对象内方法并传递参数实现接口的远程调用。

