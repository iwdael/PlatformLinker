package com.iwdael.platformlinker

import java.io.File
import java.io.FileWriter

object FinalWxAcClassBuilder {
    private const val PLACEHOLDER = "#PACKAGE#"
    private const val CONTENT = "package ${PLACEHOLDER};\n" +
            "import android.os.Bundle;\n" +
            "import com.iwdael.platformlinker.Authorize;\n" +
            "import com.iwdael.platformlinker.Platform;\n" +
            "import com.iwdael.platformlinker.Platforms;\n" +
            "import com.iwdael.platformlinker.TransparentActivity;\n" +
            "import java.util.List;\n" +
            "import java.util.Map;\n" +
            "\n" +
            "public class WXEntryActivity extends TransparentActivity {\n" +
            "    @Override\n" +
            "    protected void onCreate(Bundle savedInstanceState) {\n" +
            "        super.onCreate(savedInstanceState);\n" +
            "        Map<Authorize, Platform> platforms = Platforms.Companion.getInstance();\n" +
            "        for (Map.Entry<Authorize, Platform> entry : platforms.entrySet()) {\n" +
            "            entry.getValue().handleIntent(getIntent());\n" +
            "        }\n" +
            "        finish();\n" +
            "    }\n" +
            "}\n"

    fun brewJava(outputDir: File, packageName: String) {
        val dir = File(outputDir, packageName.replace(".", File.separator)).apply { mkdirs() }
        val wxJava = File(dir, "WXEntryActivity.java")
        wxJava.deleteOnExit()
        FileWriter(wxJava).append(CONTENT.replace(PLACEHOLDER, packageName))
            .apply {
                flush()
                close()
            }
    }
}