图片资源名称尽量用UI提供的，如果后续有换肤或者与资源文件有关的功能会方便点，和ui交代一下Android这边的图片命名规范就行
除特殊情况外，应禁止在局部写死,包括颜色、文件路径、参数key等，要写到单独的配置文件中

color资源文件命名规则：
    color.xml中的以颜色的用途命名
    color文件夹中的颜色文件：功能_状态1颜色_状态2颜色
实体类命名:
    上传的参数：实体/功能+Request+Bean
    返回的数据：实体/功能+Response+Bean
    本地使用的实体：实体/功能+Bean
使用:
    继承基本类并传入databinding和vm，vm中传入repository,免去初始化的工作

MVVM:
    model:存放实体类，可配合databinding实现与控件的双向绑定，改变数据或者改变控件的文本内容，可同时修改对方的内容
    view:layout文件
    vm(ViewModel):定义变量、针对页面定义数据操作api
    repository:实现具体的数据操作逻辑供viewmodel调用，不管是调接口还是本地数据库都是在这个文件里实现
    databinding:减少findviewbyid的编写，实现数据与控件的绑定，可以将数据变量定义在viewmodel中，并传入layout文件中进行双向绑定，
                实现view与model的交互，减少activity的代码量，activity只需要实现界面的更新
                xml中的控件的属性可以使用表达式进行数据判断，减少activity中的逻辑量
    livedata:通过setValue/postValue实现数据更新观察，activity中只需要观察相关结果的更新，减少接口调用回调方法的定义
    Room:内部数据库
