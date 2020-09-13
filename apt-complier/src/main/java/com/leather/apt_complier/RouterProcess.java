package com.leather.apt_complier;

import com.google.auto.service.AutoService;
import com.leather.apt_annotation.Router;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class RouterProcess extends AbstractProcessor {
    private static final String CREATE_CLASS_NAME = "RouterUtil";
    private static final String CREATE_PACKAGE_NAME = "com.leather.aptdemo.util";
    //存储路由地址和对应Element的映射表
    private Map<String, TypeElement> routerMap = new HashMap<>();
    //用来创建文件
    private Filer filer;
    //用来打印日志
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
        if (elements == null || elements.isEmpty()) {
            return false;
        }

        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            String path = typeElement.getAnnotation(Router.class).path();
            if (!routerMap.containsKey(path)) {
                routerMap.put(path, typeElement);
            }
        }
        createRouterFile();
        return true;
    }

    /**
     * 创建路由文件
     */
    private void createRouterFile() {
        if (routerMap.isEmpty()) {
            //如果映射表为空则不创建
            messager.printMessage(Diagnostic.Kind.ERROR, "映射表为空则不创建");
            return;
        }

        Writer writer = null;
        try {
            //为避免类名重复，生成的类名加上动态时间戳---此处实现与ARouter本身不一致，但更简单。
            //避免了从build.gradle中传递参数的步骤
            String activityName = CREATE_CLASS_NAME + System.currentTimeMillis();
            JavaFileObject sourceFile = filer.createSourceFile(CREATE_PACKAGE_NAME + "." + activityName);
            writer = sourceFile.openWriter();
            //代码生成
            StringBuilder routerBuild = new StringBuilder();
            for (String key : routerMap.keySet()) {
                routerBuild.append("        MyRouter.getInstance().addRouter(\"" + key + "\", " + routerMap.get(key).getQualifiedName() + ".class" + ");\n");
            }


            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("package " + CREATE_PACKAGE_NAME + ";\n");
            stringBuilder.append("import com.leather.aptdemo.MyRouter;\n" +
                    "import com.leather.aptdemo.IRouter;\n" +
                    "\n" +
                    "public class " + activityName + " implements IRouter {\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void loadInto() {\n" +
                    routerBuild.toString() +

                    "    }\n" +
                    "}");
            writer.write(stringBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //返回最新的版本
        return SourceVersion.latestSupported();
    }

    /**
     * @return 返回支持的注解类型。在这里就是我们定义的所有被Router所注解的类
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> typeSet = new HashSet<>();
        //这个api是获取完整的包名+类名
        String canonicalName = Router.class.getCanonicalName();
        typeSet.add(canonicalName);
        return typeSet;
    }
}