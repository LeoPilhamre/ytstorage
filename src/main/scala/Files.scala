package ytstorage

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import javax.imageio.stream.FileImageOutputStream
import javax.imageio.ImageWriteParam
import scala.collection.mutable.ArrayBuffer


object Files:
	def read(path: String): File =
		new File(path)

	def store(image: BufferedImage, path: String): Boolean =
		ImageIO.write(image, "png", new File(path))

	extension (file: File) def toLines: Iterator[String] = ???


case class Frames(frames: Seq[BufferedImage]):
	def apply(i: Int) = frames(i)

	def exportAsMOV(path: String, frameRate: Int): Unit =
		val fileOut = new FileImageOutputStream(new File(path))
		
		try
			val writer = ImageIO.getImageWritersByFormatName("mov").next()
			writer.setOutput(fileOut)
			
			val param = writer.getDefaultWriteParam.asInstanceOf[ImageWriteParam]
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT)
			param.setCompressionType("Animation")
			param.setCompressionQuality(1.0f)

			writer.prepareWriteSequence(null)
			
			// write to sequence
			for (image <- frames)
				writer.writeToSequence(new javax.imageio.IIOImage(image, null, null), param)
			
			writer.endWriteSequence()

		finally
			// close stream
			if (fileOut != null)
				fileOut.close()


object Frames:
	extension [T <: Seq[BufferedImage]](frames: T)
		def toFrames: Frames = Frames(frames)