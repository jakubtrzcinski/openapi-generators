package io.trzcinski.oasgen.utils

class StringUtils {
    companion object {
        fun kebabToCamel(str: String): String {
            var working = str;
            while (working.indexOf("-") != -1) {
                val index = working.indexOf("-");
                working = working.substring(0, index) + working.substring(index + 1).capitalize()
            }
            return working;
        }
    }
}