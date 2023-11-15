package ytstorage

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage


object Files:
	def read(path: String): File =
		new File(path)

	def store(image: BufferedImage, path: String): Boolean =
		ImageIO.write(image, "png", new File(path))

	extension (file: File) def toLines: Iterator[String] = ???