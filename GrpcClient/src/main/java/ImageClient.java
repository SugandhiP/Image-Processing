import com.google.protobuf.ByteString;
import com.image.grpc.ImgProto;
import com.image.grpc.imageGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class ImageClient {
    public static void main(String[] args){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext().build();
        imageGrpc.imageStub newStub = imageGrpc.newStub(channel);

        try {
            cleanup();
            Scanner kb = new Scanner(System.in);
            System.out.println("Ensure that you have uploaded your image in the /resources/InputImage folder\nEnter Image Name:");
            String imageName = kb.next();
            List<String> operations = takeInputOperations(kb);
            BufferedImage inputImage = ImageIO.read(ImageClient.class.getResourceAsStream("/InputImage/"+imageName));

            byte[] imageData = toByteArray(inputImage); // Replace with actual image byte array


            if(imageData == null) {
                System.out.println("No Image at client end");
            }

            ImgProto.ImageRequest.Builder requestBuilder = ImgProto.ImageRequest.newBuilder()
                    .setImageData(com.google.protobuf.ByteString.copyFrom(imageData))
                    .setImageName(imageName);
            for (String operation : operations) {
                requestBuilder.addOperations(operation);
            }
            ImgProto.ImageRequest request = requestBuilder.build();

            //ImgProto.ImageResponse response = imageStub.imgProcess(request);

            String outputFolder = "OutputFolder";
            // Create the output directory if it doesn't exist
            File outputDir = new File(outputFolder);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            CountDownLatch finishLatch = new CountDownLatch(1);
            StreamObserver<ImgProto.ImageResponse> responseObserver = new StreamObserver<ImgProto.ImageResponse>() {
                @Override
                public void onNext(ImgProto.ImageResponse imageResponse) {
                    try {
                        // Receive the main image and save it
                        byte[] mainImageData = imageResponse.getMainImageData().toByteArray();
                        BufferedImage mainImage = ImageIO.read(new java.io.ByteArrayInputStream(mainImageData));
                        if (mainImage != null) {
                            String mainImageFileName = outputDir + File.separator + "main_image.jpg";
                            File mainImageFile = new File(mainImageFileName);
                            ImageIO.write(mainImage, "jpg", mainImageFile);
                            System.out.println("output dir path: "+mainImageFile);
                        }

                        // Receive and save the thumbnail images
                        int i = 0;
                        for (ByteString thumbnailImageData : imageResponse.getThumbnailImageDataList()) {
                            BufferedImage thumbnailImage = ImageIO.read(new java.io.ByteArrayInputStream(thumbnailImageData.toByteArray()));
                            if(thumbnailImage != null) {
                                String thumbnailImageFileName = outputDir + File.separator + "thumbnail_image_" + i + ".jpg";
                                System.out.println("output dir path: "+thumbnailImageFileName);
                                File thumbnailImageFile = new File(thumbnailImageFileName);
                                ImageIO.write(thumbnailImage, "jpg", thumbnailImageFile);
                            }
                            i++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                    finishLatch.countDown();
                }

                @Override
                public void onCompleted() {
                    finishLatch.countDown();
                }
            };
            newStub.imgProcess(request,responseObserver);
            System.out.println("Image processing completed, Check OutputFolder for the resultant output images");
            finishLatch.await();

        } catch (Exception e) {
            System.err.println("[Error] Processing image output: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private static List<String> takeInputOperations(Scanner kb) {
       List<String> myOps = new ArrayList<>();
        int valid = 0;
        while(valid == 0) {
            System.out.println("Enter the operation you want to perform (flip, rotate, resize, thumbnail, grayscale, rotate left, rotate right). \nPress exit to stop.\n");
            String task = kb.next().trim().toLowerCase(Locale.ROOT);
            switch(task) {
                case "flip":
                    System.out.println("flip Horizontally or Vertically choose one: (flipv | fliph)");
                    String flip = kb.next();
                    valid = validateFlip(flip);
                    if(valid != 0) {
                        valid = 0;
                        break;
                    }
                    myOps.add(flip);
                    break;
                case "rotate":
                    System.out.println("Enter Rotate Percent (Eg. -100, +365, 200. RANGE -1000 to +1000):");
                    String rotate = kb.next();
                    valid = validateRotate(rotate);
                    if(valid != 0) {
                        valid = 0;
                        break;
                    }
                    myOps.add(task+rotate);
                    break;
                case "resize":
                    System.out.println("Enter Resize Percent (Eg. -100, +365, 200. RANGE -95 to 500):");
                    String resize = kb.next();
                    valid = validateResize(resize);
                    if(valid != 0) {
                        valid = 0;
                        break;
                    }
                    myOps.add(task+resize);
                    break;
                case "grayscale":
                    myOps.add(task);
                    break;
                case "thumbnail":
                    myOps.add(task);
                    break;
                case "rotateleft":
                    myOps.add("left");
                    break;
                case "rotateright":
                    myOps.add("right");
                    break;
                case "exit":
                    valid = -1;
                    break;
                default:
                    System.out.println("Invalid Operation: "+task);
                    valid = 0;
                    break;
            }
        }
        if(myOps.isEmpty()) {
            System.out.println("You have not selected any operations.");
            takeInputOperations(kb);
        }
        return myOps;
    }

    private static int validateResize(String operation) {
        if (!(Integer.valueOf(operation) > -95 && Integer.valueOf(operation) < 500)) {
            System.out.println("Invalid operation resize" + operation);
            return -1;
        } else {
            System.out.println("Valid operation resize" + operation);
            return 0;
        }
    }

    private static int validateFlip(String operation) {
        switch (operation.trim().toLowerCase(Locale.ROOT)) {
            case "flipv":
                return 0;
            case "fliph":
                return 0;
            default:
                System.out.println("Invalid operation: " + operation);
                return -1;
        }
    }

    private static int validateRotate(String operation){
        if (!(Integer.valueOf(operation) > -1000 && Integer.valueOf(operation) < 1000))
            return -1;
        else {
            System.out.println("Valid operation checked in validation: " + operation);
            return 0;
        }
    }

    private static void cleanup(){
        File directory = new File("OutputFolder");
        File[] files = directory.listFiles();
        if(files.length!=0) {
            for(File f: files) {
            f.delete();
            }
            System.out.println("Client output directory is cleaned.");
        }
    }
}
