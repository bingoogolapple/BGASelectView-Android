:running:BGASelectView-Android v1.0.0:running:
============
产品要实现类似web开发中的select标签实现的级联效果，没研究出Android自带的Spinner如何定制右侧的箭头，所以就通过继承TextView，并监听其点击事件来弹出PopupWindow实现这种效果了。


#### 效果图
![Demo](https://raw.githubusercontent.com/bingoogolapple/BGASelectView-Android/master/screenshots/bgaselectview.gif)

>Gradle

```groovy
dependencies {
    compile 'cn.bingoogolapple:bga-selectview:1.0.0@aar'
}
```

##### 详细用法请查看[Demo](https://github.com/bingoogolapple/BGASelectView-Android/tree/master/demo):feet:
