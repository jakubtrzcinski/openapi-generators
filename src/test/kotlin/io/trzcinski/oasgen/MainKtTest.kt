package io.trzcinski.oasgen

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.*


internal class MainKtTest {

    @Test
    fun test_kotlin(){
        main(arrayOf("gen","https://petstore.swagger.io/v2/swagger.json", "out/src/main/kotlin/io/trzcinski/test"))
        verifyDirsAreEqual(
            Path.of("out/src/main/kotlin/io/trzcinski/test"),
            Path.of("test-samples/kotlin-retrofit/src/main/kotlin/io/trzcinski/test"),
        )
        File("out").deleteRecursively()
    }
    @Test
    fun test_dart(){
        main(arrayOf("gen","https://petstore.swagger.io/v2/swagger.json", "out/shared/api"))
        verifyDirsAreEqual(
            Path.of("out/shared/api"),
            Path.of("test-samples/dart-retrofit/shared/api"),
        )
        File("out").deleteRecursively()
    }


    @Throws(IOException::class)
    private fun verifyDirsAreEqual(one: Path, other: Path) {
        Files.walkFileTree(one, object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun visitFile(
                file: Path,
                attrs: BasicFileAttributes
            ): FileVisitResult {
                val result = super.visitFile(file, attrs)

                // get the relative file name from path "one"
                val relativize: Path = one.relativize(file)
                // construct the path for the counterpart file in "other"
                val fileInOther: Path = other.resolve(relativize)
                val otherBytes: ByteArray = Files.readAllBytes(fileInOther)
                val theseBytes: ByteArray = Files.readAllBytes(file)
                if (!Arrays.equals(otherBytes, theseBytes)) {
                    throw AssertionFailedError(file.toString() + " is not equal to " + fileInOther)
                }
                return result
            }
        })
    }
}