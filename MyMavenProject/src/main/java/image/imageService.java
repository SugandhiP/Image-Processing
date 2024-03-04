package image;

import com.image.grpc.ImgProto;
import com.image.grpc.imageGrpc;
import io.grpc.stub.StreamObserver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class imageService extends imageGrpc.imageImplBase{
    @Override
    public void imgProcess(ImgProto.ImageRequest request , StreamObserver<ImgProto.ImageResponse> responseObserver) {
        System.out.println("Inside imgProcess");
        try {
            BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(request.getImageData().toByteArray()));

            if(inputImage==null) {
                System.out.println("No Image at server end");
            }

            if(!request.getOperationsList().isEmpty()) {
                List<String> operations = request.getOperationsList();
                String msg = performOperation(inputImage, operations);
                System.out.println("Result: "+msg);
            }


            //Load output image
            File outputFile = new File("outputImages", "output_image.jpg");
            BufferedImage bufferedImage = ImageIO.read(outputFile);


            // read thumbnails
            List<BufferedImage> thumbnails = readThumbnails();


            // Convert images to byte arrays
            byte[] mainImageData = toByteArray(bufferedImage);

            // Create and send the response
            ImgProto.ImageResponse.Builder responseBuilder = ImgProto.ImageResponse.newBuilder()
                    .setMainImageData(com.google.protobuf.ByteString.copyFrom(mainImageData));

            if(!thumbnails.isEmpty()) {
                List<byte[]> thumbnailImageDatas = new ArrayList<>();
                for (BufferedImage thumbnail : thumbnails) {
                    thumbnailImageDatas.add(toByteArray(thumbnail));
                }
                for (byte[] thumbnailImageData : thumbnailImageDatas) {
                    responseBuilder.addThumbnailImageData(com.google.protobuf.ByteString.copyFrom(thumbnailImageData));
                }
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

            //cleanup();
        } catch (Exception e) {
            System.err.println("[Error] Processing image: " + e.getMessage());
            e.printStackTrace();
            //responseObserver.onError(Status.INTERNAL.withDescription("Error processing image: " + e.getMessage()).asRuntimeException());
        }
    }


    public static String performOperation(BufferedImage inputImage, List<String> operations) {
        try {
            System.out.println("Inside perform Operations");
            int thumbnailCount = 0;
            double rotatePercentage = 0.0;
            double resizePercentage = 0.0;

            for(int i=0; i<operations.size(); i++) {
                String performTask = operations.get(i);
                if(operations.get(i).startsWith("rotate")) {
                    rotatePercentage = Double.valueOf(operations.get(i).substring(6));  //extractPercentage(performTask);
                    performTask = "rotate";
                }else if(operations.get(i).startsWith("resize")) {
                    resizePercentage = Double.valueOf(operations.get(i).substring(6));
                    performTask = "resize";
                }
                switch(performTask)
                {
                    case "flipv":
                        System.out.println("Inside switch case");
                        flipVertically(inputImage);
                        break;
                    case "fliph":
                        flipHorizontally(inputImage);
                        break;
                    case "left":
                        rotateByPercentage(inputImage,-90);
                        break;
                    case "right":
                        rotateByPercentage(inputImage,+90);
                        break;
                    case "grayscale":
                        grayscale(inputImage);
                        break;
                    case "thumbnail":
                        generateThumbnail(thumbnailCount);
                        ++thumbnailCount;
                        break;
                    case "rotate":
                        rotateByPercentage(inputImage,rotatePercentage);
                        break;
                    case "resize":
                        resizeByPercentage(inputImage, resizePercentage);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operation " + operations.get(i));
                }
                File outputFile = new File("outputImages", "output_image.jpg");
                inputImage = ImageIO.read(outputFile);
            }

        } catch (Exception e) {
            System.err.println("[Error] Error in Switch case: " + e.getMessage());
            e.printStackTrace();
        }
        return "Image Flipped Successfully";
    }

    public static void flipVertically(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, originalImage.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flippedImage.setRGB(x, height - 1 - y, originalImage.getRGB(x, y));
            }
        }

        // Save the output image to a directory within the project structure
        saveImage(flippedImage, "output_image.jpg");
    }


    public static void flipHorizontally(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, originalImage.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flippedImage.setRGB(width - 1 - x, y, originalImage.getRGB(x, y));
            }
        }

        // Save the output image to a directory within the project structure
        saveImage(flippedImage, "output_image.jpg");
    }

    public static void resizeByPercentage(BufferedImage originalImage, double percentage) {
        // Calculate new width and height based on the percentage
        double scaleFactor = 1.0 + (percentage / 100.0);

        int newWidth = (int) (originalImage.getWidth() * scaleFactor);
        int newHeight = (int) (originalImage.getHeight() * scaleFactor);

        //Image tmp = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        //BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        // Save the output image to a directory within the project structure
        saveImage(resizedImage, "output_image.jpg");
    }

    public static void grayscale(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        Graphics g = grayscaleImage.getGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();

        // Save the output image to a directory within the project structure
        saveImage(grayscaleImage, "output_image.jpg");
    }

    public static void rotateByPercentage(BufferedImage originalImage, double percentage) {
        double angle = (360 * (percentage / 100.0)); // Calculate the angle of rotation

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage rotatedImage = new BufferedImage(width, height, originalImage.getType());

        Graphics2D g = rotatedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), width / 2.0, height / 2.0); // Rotate about the center

        g.drawImage(originalImage, transform, null);
        g.dispose();

        // Save the output image to a directory within the project structure
        saveImage(rotatedImage, "output_image.jpg");
    }


    public static void generateThumbnail(int index) {
        try {
            // Load the original image
            File originalImagePath = new File("outputImages", "output_image.jpg");
            BufferedImage originalImage = ImageIO.read(originalImagePath);

            // Create a thumbnail image
            BufferedImage thumbnail = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = thumbnail.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, 200, 200, null);
            g.dispose();

            // Save the thumbnail image
            String directory = "outputImages";
            File outputFolderFile = new File(directory);
            if (!outputFolderFile.exists()) {
                outputFolderFile.mkdirs();
            }
            File outputFile = new File(directory + "/thumbnail_" + index + ".jpg");
            ImageIO.write(thumbnail, "jpg", outputFile);
            System.out.println("Thumbnail saved: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void saveImage(BufferedImage image, String fileName) {
        try {
            String directory = "outputImages";
            // Create the output directory if it doesn't exist
            File outputDir = new File(directory);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Save the image to the specified directory
            File outputFile = new File(outputDir, fileName);
            ImageIO.write(image, "jpg", outputFile);
        } catch (IOException e) {
            System.out.println("[Error] Failed to save the resultant image");
            e.printStackTrace();
        }

    }


    private static List<BufferedImage> readThumbnails() throws IOException {
        String directory = "outputImages";
        List<BufferedImage> thumbnails = new ArrayList<>();
        // Create the output directory if it doesn't exist
        File outputDir = new File(directory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // read the thumbnail images
        File[] files = outputDir.listFiles();
        for (File file : files) {
            if(file.isFile() && file.getName().startsWith("thumbnail_")) {
                try {
                    BufferedImage thumbnail = ImageIO.read(file);
                    if (thumbnail != null) {
                        thumbnails.add(thumbnail);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading thumbnail: " + e.getMessage());
                }
            }
        }
        return thumbnails;
    }

    private byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static int extractPercentage(String input) {
        // Regular expression to match "rotate" followed by an optional plus or minus sign and a number
        Pattern pattern = Pattern.compile("(resize|rotate)([+-]?\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String angleString = matcher.group(2); // Extract the matched group containing the percentage
            int angle = Integer.parseInt(angleString);
            return angle;
        }

        // If no match found, return 0
        return 0;
    }

    private static void cleanup() {
        File directory = new File("outputImages");
        File[] files = directory.listFiles();

        for(File f: files) {
            f.delete();
        }
        System.out.println("Server cleanup was successful.");

    }
}
