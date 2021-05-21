### 重置过的WanAndroid客户端，原项目:https://github.com/ShowMeThe/WanAndroid
这个客户端大体上是修改了旧客户端的一些Bug和简化代码,预览图如下：</br>
<img src="https://github.com/ShowMeThe/WanAndroid/blob/master/theme1.gif" width ="200" alt="GZA9mT.gif" border="0" /></br>
这个版本大部分内容都是建立在Databinding下，但是改善了过分使用的情况。逻辑也从Databinding中抽出来，例如利用LiveData和Extra方法去更新UI的操作，</br>
虽然这个特性是Databinding的特色，但是还是被我移出来了。</br>
### 更新日志：
#### 2020/4/8：修改不合理设计</br>
#### 2020/4/5：皮肤切换添加支持json输入</br>
规则大致如下
```
{
  "theme_viewGroup_background": "ff3d00",
  "theme_viewGroup_backgroundColor": "ff3d00",
  "theme_card_strokeColor": "ff3d00",
  "theme_text_color": "ff3d00",
  "theme_button_textColor": "ff3d00",
  "theme_button_rippleColor": "2cf4511e",
  "theme_button_iconTint": "ff3d00",
  "theme_button_strokeColor": "ff3d00",
  "theme_bottom_navigation_iconTint": "ff3d00",
  "theme_bottom_navigation_textColor": "ff3d00",
  "theme_imageView_tint": "ff3d00",
  "theme_floating_backgroundColor": "ff3d00",
  "theme_edit_cursorDrawable": "ff3d00",
  "theme_edit_highlightColor": "ff3d00",
  "theme_inputLayout_boxColor": "ff3d00",
  "theme_inputLayout_hintColor": "ff3d00",
  "colorObjects": [
    "ff3d00",
    "2cf4511e"
  ]
}
```
示例代码：
```
        val json = AssetFile.getJson(this,"orange.json")
        val colorEntity = json.fromJson<ColorEntity>()!!
        val json2 = AssetFile.getJson(this,"yellow.json")
        val colorEntity2 = json2.fromJson<ColorEntity>()!!
        SkinManager.init(this).addJson(themes_name[3] to colorEntity, themes_name[4] to colorEntity2)
```
### 无缝切换皮肤方案
这个无缝切换皮肤不是利用修改attr那些方法，而是利用Databinding的拓展方法进行的，所以这个方法不打算另外开出来细讲，因为就是利用拓展函数的便利，</br>
进行对应的控件属性的设置方法统一管理，所以只覆盖了该项目用到的控件，因为Android的属性很多，所以未能完全覆盖。你们可以自行看看代码，研究看看。</br>
因为适用的场景少，而且受限于Databinding，所以不一定适合作为生产环境的的一个解决方案。</br>
配置大致如下
```

     <style name="MaterialTheme.Blue">
         <item name="theme_text_color">@color/colorAccent</item>
         <item name="theme_viewGroup_backgroundColor">@color/colorAccent</item>
         <item name="theme_viewGroup_background">@drawable/shape_drawer_head_bg</item>
         <item name="theme_button_rippleColor">@color/color_5f4fc3f7</item>
         <item name="theme_button_iconTint">@color/colorAccent</item>
         <item name="theme_button_textColor">@color/colorAccent</item>
         <item name="theme_button_strokeColor">@color/colorAccent</item>
         <item name="theme_bottom_navigation_iconTint">@color/colorAccent</item>
         <item name="theme_bottom_navigation_textColor">@color/colorAccent</item>
         <item name="theme_imageView_tint">@color/colorAccent</item>
         <item name="theme_card_strokeColor">@color/colorAccent</item>
         <item name="theme_floating_backgroundColor">@color/colorAccent</item>
         <item name="theme_edit_cursorDrawable">@drawable/shape_blue_cursor</item>
         <item name="theme_inputLayout_boxColor">@color/colorAccent</item>
         <item name="theme_inputLayout_hintColor">@color/colorAccent</item>
         <item name="theme_edit_highlightColor">@color/colorAccent</item>
    </style>

```  
以登录界面为例子：</br>
```
 <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/tiName"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/px200dp"
                android:layout_marginEnd="@dimen/px110dp"
                android:layout_marginStart="@dimen/px110dp"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                skin="@{`boxColor|hintColor`}"
                >
                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/edName"
                    android:text="@={Login.account}"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    skin="@{`cursor`}"
                    />

            </com.google.android.material.textfield.TextInputLayout>
```  
其中的boxColor和hitColor是修改TextInputLayout的外边框颜色，cursor是修改光标图片，</br>
题外话如果是使用 style="@style/Widget.MaterialComponents.TextInputLayout.FilledBox.Dense"</br>
的话那个高亮的底线是需要和boxColor一起修改，即TextInputLayout的setBoxStrokeColor和TextView的setHighlightColor使用，没错你们看错，是TextView,我采用反射的操作方法进行修改的。
### @InjectOwner这个注解和VMRouter的使用
这个InjectOwner是我配合利用反射，初始化数据仓库class *** : BaseRepository

### CallResult的改进
增加合并的接口的处理，通过注解viewModel的对象到仓库，androidScope中通过getLifeOwner获取预先注入的owner，viewmodel对应着owner,Fragment共用Activity的owner
```
  fun androidScope(scope:LifecycleOwner?.()->Unit){
        if(owner == null){
            owner = getLifeOwner(viewModel)
            owner?.lifecycle?.addObserver(this)
        }
        scope.invoke(owner)
    }



  fun getHomeArticle(page: Int, liveData: KResultData<JsonData<Article>>) {
        androidScope {
            callResult {
                hold { api.getHomeArticle(page) }
                    .bindData(liveData)
            }
        }
    }

    fun getArticleTop(liveData: KResultData<List<DatasBean>>) {
        androidScope {
            callResult {
                merge({ api.getHomeArticle(0) },
                    { api.getHomeTop() },
                    object :
                        IFunction<JsonData<Article>, JsonData<List<DatasBean>>, List<DatasBean>> {
                        override fun apply(
                            t1: JsonData<Article>?,
                            t2: JsonData<List<DatasBean>>?
                        ): List<DatasBean> {
                            val list = ArrayList<DatasBean>()
                            t2?.apply {
                                if (isLegal() && data != null) {
                                    data!!.forEach {
                                        it.top = true
                                    }
                                    list.addAll(data!!)
                                }
                            }
                            t1?.apply {
                                if (isLegal() && data != null) {
                                    list.addAll(data!!.datas)
                                }
                            }
                            return list
                        }

                    }).bindData(liveData)
            }
        }
    }

```  
post的方法可以把MutableLiveData<KResult<*>> 更新数据，所以使用时候需要谨慎处理response null的问题。</br>
而Retrofit api接口的初始化，我也改善了，如下:
```
//初始化
   initScope {
            single { Http.createApi(Main::class.java) }
        }
        
//调用single() ，全局获取单例的接口对象
private val api : Main by single()

```

