package ytstorage

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import javax.imageio.stream.FileImageOutputStream
import javax.imageio.ImageWriteParam
import scala.collection.mutable.ArrayBuffer
import java.io.FileInputStream
import java.io.BufferedInputStream
import com.xuggle.mediatool.{IMediaWriter, ToolFactory}
import com.xuggle.xuggler.{Global, ICodec}
import java.util.concurrent.TimeUnit

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.demos.DecodeAndPlayVideo;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT
import java.util.concurrent.TimeUnit.MILLISECONDS
import com.xuggle.mediatool.IMediaWriter
import com.xuggle.mediatool.ToolFactory
import com.xuggle.mediatool.IMediaViewer
import scala.collection.mutable.Buffer


object Files:
	def readAll(path: String, recursive: Boolean = false): Vector[File] =
		var fileAtPath = File(path)
		if fileAtPath.isDirectory() then
			if recursive then
				val all = ArrayBuffer[File]()

				val files = fileAtPath.listFiles()
				for file <- files do
					all.addAll(readAll(file.getAbsolutePath, recursive))

				all.toVector
			else
				fileAtPath.listFiles().toVector
		else
			Vector(fileAtPath)

	def read(path: String): File =
		new File(path)

	def store(image: BufferedImage, path: String): Boolean =
		ImageIO.write(image, "png", new File(path))

	extension (file: File)
		def toBytes: Vector[Byte] =
			val stream = new BufferedInputStream(new FileInputStream(file))
			val bytes: Array[Byte] = stream.readAllBytes()
			stream.close()

			bytes.toVector


case class Frames(frames: Seq[BufferedImage]):
	def apply(i: Int) = frames(i)

	def exportAsMOV(path: String, frameRate: Int): Unit =
		require(frames.length > 0)

		var nextFrameTime: Long = 0L

		val videoStreamIndex = 0
		val videoStreamId = 0
		val rate: Long = DEFAULT_TIME_UNIT.convert(1000 / frameRate, MILLISECONDS)
		val width = frames(0).getWidth
		val height = frames(0).getHeight

		try
			val writer: IMediaWriter = ToolFactory.makeWriter(path)
			writer.addVideoStream(videoStreamIndex, videoStreamId, width, height)

			frames.foreach(frame => {
				writer.encodeVideo(videoStreamIndex, frame, nextFrameTime, DEFAULT_TIME_UNIT)
				nextFrameTime += rate
			})

			writer.close()
		catch
			case e: Exception => e.printStackTrace()

		/*val writer: IMediaWriter = ToolFactory.makeWriter(path)
		val streamId = writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, IRational.make(frames.size / frameRate), 640, 480)

		var frameIndex = 0
		frames.foreach { frame =>
			writer.encodeVideo(streamId, frame, frameIndex * 1000, TimeUnit.MILLISECONDS)
			frameIndex += 1
		}

		writer.close()*/

		/*val fileOut = new FileImageOutputStream(new File(path))
		
		try
			val writer = ImageIO.getImageWritersByFormatName("png").next()
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
				fileOut.close()*/


object Frames:
	extension [T <: Seq[BufferedImage]](frames: T)
		def toFrames: Frames = Frames(frames)