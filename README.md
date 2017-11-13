# example
Activity+Fragment典型app框架

Activity页面：
继承BaseActivity
LaunchActivity：启动页面，加载用户数据，更新检测等操作在此界面实现，操作结束后根据实际业务决定跳转GuideActivity、HomePageActivity或者DetailActivity。
GuideActivity：首次启动的多图引导页，内含一个ViewPager，图片根据需求增删，最后一个页面根据业务跳转HomePageActivity或者DetailActivity。
HomePageActivity：主界面，页面数量由底部导航功能块决定，改页面展示界面一定包含底部导航栏，任意功能块内二次跳转均使用DetailActivity展示处理。
DetailActivity：内容管理核心展示页面，展示内容由单个Fragment实现后嵌套该页面。

Fragment页面：
继承BaseFragment
使用FragmentMapManager.java单例维护，获取实例方式采取反射

获取对象方法：
FragmentMapManager fmm = FragmentMapManager.getInstance();
Fragment fragment = fmm.getFragment(RegisterFragment.class)或
Fragment fragment = fmm.getFragment(RegisterFragment.class.getName())；

页面的跳转：
使用setTransactionFragment(Class<?> cls)或
setTransactionFragment(String classname)直接跳转指定页面

传递对象：
setTransactionFragment(RegisterFragment.class, intent)或
setTransactionFragment(RegisterFragment.class.getName(),intent)
然后在需要接受接收数据的Fragment中实现父类onIntent(Intent intent)方法，接收Intent对象。

从其他Activity中跳转：
setDetailsFragment()

back键的处理，必须实现BaseFragment内的抽象方法onBackPressed()，在该方法内处理是否对系统back键的截断，若有需要可更改为return true则可拦截系统back键。处理完事务后务必确保return false。否则back失效。

用户管理中心 UserInfo.java
对象获取：UserInfo userInfo = UserInfo.getInstance(context)
属性获取：userInfo.XX
属性更新：userInfo.XX = YY;
UserInfo.getInstance(context).upDataUserInfo();

第三方框架
线程通信：EventBus3.0
Http客户端：retrofit2.0 建议搭配GsonFormat插件使用
网络请求：OkHttp3
View注入：Butter Knife



