package showmethe.github.core.widget.slideback.annotation

@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class SlideBackBinder(val transparent:Boolean = false)