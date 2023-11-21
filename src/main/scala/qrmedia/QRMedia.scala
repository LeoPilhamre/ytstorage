package ytstorage.lib.qrmedia

import java.awt.image.BufferedImage
import ytstorage.Files.*
import ytstorage.Files
import java.io.File
import com.google.zxing.qrcode.encoder.{QRCode, Encoder}
import scala.collection.mutable.ArrayBuffer
import ytstorage.Frames
import ytstorage.Frames.*
import com.google.zxing.qrcode.decoder.*
import java.util.Base64.getEncoder
import com.google.zxing.EncodeHintType
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.MultiFormatWriter
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.encoder.ByteMatrix
import com.google.zxing.common.BitMatrix
import ytstorage.utils.*
import com.google.zxing.client.j2se.MatrixToImageConfig
import scala.collection.mutable.ListBuffer

case class QRMedia(version: Int, files: File*):
	import QRMedia.*

	lazy val max: Int =
		val v = Version.getVersionForNumber(version)
		val ec = v.getECBlocksForLevel(ErrorCorrectionLevel.L)
		v.getTotalCodewords - ec.getTotalECCodewords - 8 // correction

	/**
	  * Encode `file` ... TODO
	  *
	  * @param width
	  * @param height
	  * @param alignment
	  * @param allowMultiple
	  * @return
	  */
	def encode(
		width: Int = 512,
		height: Int = 512,
		scale: Int = 1,
		alignment: Alignment = Alignment.Cover,
		allowMultiple: Boolean = true,
		charset: String = "UTF-8"
	): Frames =
		val frames: ListBuffer[BufferedImage] = ListBuffer[BufferedImage]()
		
		val hints = new java.util.HashMap[EncodeHintType, Any]
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L)
		
		val bounds = Dim(width, height)

		for file <- files do
			val bytes = file.toBytes
			var content: String = getEncoder.encodeToString(bytes.toArray)
			
			var pos = Pos(0, 0)

			var current = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

			while content.length > 0 do
				val end = math.min(max, content.length)

				val segment = content.take(end)
				content = content.drop(end)

				val qr: QRCode = Encoder.encode(segment, ErrorCorrectionLevel.L, hints)
				qr.setVersion(Version.getVersionForNumber(version))
				qr.setMode(Mode.BYTE)

				val image = MatrixToImageWriter.toBufferedImage(qr.getMatrix.toBitMatrix)
				val dim = Dim(image.getWidth * scale, image.getHeight * scale)

				// write qr code to image at pos
				val g = current.createGraphics()
				g.scale(scale, scale)
				g.drawImage(image, pos.x / scale, pos.y / scale, null)
				g.dispose()

				image.flush()

				var next = pos + Pos(dim.width, 0)
				if !bounds.isInside(next + Pos(dim.width, 0)) then
					// new row
					next = Pos(0, pos.y + dim.height)

				if !bounds.isInside(next + Pos(0, dim.height)) then
					// new frame
					frames += current
					current.flush()
					current = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
					next = Pos(0, 0)

				pos = next

			frames += current
			current.flush()
		
		frames.toSeq.toFrames
		

object QRMedia:
	enum Alignment:
		case Cover, Fit, Center

	def from(path: String, version: Int = 40): QRMedia =
		require(version > 0 && version <= 40,
			s"Version: $version. 'version' is required to be in range (0, 40].")

		val files = Files.readAll(path, recursive=true)
		new QRMedia(version, files*)

	
	extension (matrix: ByteMatrix)
		def toBitMatrix: BitMatrix =
			val width = matrix.getWidth
			val height = matrix.getHeight
			val bitMatrix = new BitMatrix(width, height)
			for x <- 0 until width do
			
				for y <- 0 until height do
					if matrix.get(x, y) == 1 then
						bitMatrix.set(x, y)
			
			bitMatrix