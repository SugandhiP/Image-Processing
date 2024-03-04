package com.image.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: imgProto.proto")
public final class imageGrpc {

  private imageGrpc() {}

  public static final String SERVICE_NAME = "image";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.image.grpc.ImgProto.ImageRequest,
      com.image.grpc.ImgProto.ImageResponse> getImgProcessMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "imgProcess",
      requestType = com.image.grpc.ImgProto.ImageRequest.class,
      responseType = com.image.grpc.ImgProto.ImageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.image.grpc.ImgProto.ImageRequest,
      com.image.grpc.ImgProto.ImageResponse> getImgProcessMethod() {
    io.grpc.MethodDescriptor<com.image.grpc.ImgProto.ImageRequest, com.image.grpc.ImgProto.ImageResponse> getImgProcessMethod;
    if ((getImgProcessMethod = imageGrpc.getImgProcessMethod) == null) {
      synchronized (imageGrpc.class) {
        if ((getImgProcessMethod = imageGrpc.getImgProcessMethod) == null) {
          imageGrpc.getImgProcessMethod = getImgProcessMethod = 
              io.grpc.MethodDescriptor.<com.image.grpc.ImgProto.ImageRequest, com.image.grpc.ImgProto.ImageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "image", "imgProcess"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.image.grpc.ImgProto.ImageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.image.grpc.ImgProto.ImageResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new imageMethodDescriptorSupplier("imgProcess"))
                  .build();
          }
        }
     }
     return getImgProcessMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static imageStub newStub(io.grpc.Channel channel) {
    return new imageStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static imageBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new imageBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static imageFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new imageFutureStub(channel);
  }

  /**
   */
  public static abstract class imageImplBase implements io.grpc.BindableService {

    /**
     */
    public void imgProcess(com.image.grpc.ImgProto.ImageRequest request,
        io.grpc.stub.StreamObserver<com.image.grpc.ImgProto.ImageResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getImgProcessMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getImgProcessMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.image.grpc.ImgProto.ImageRequest,
                com.image.grpc.ImgProto.ImageResponse>(
                  this, METHODID_IMG_PROCESS)))
          .build();
    }
  }

  /**
   */
  public static final class imageStub extends io.grpc.stub.AbstractStub<imageStub> {
    private imageStub(io.grpc.Channel channel) {
      super(channel);
    }

    private imageStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected imageStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new imageStub(channel, callOptions);
    }

    /**
     */
    public void imgProcess(com.image.grpc.ImgProto.ImageRequest request,
        io.grpc.stub.StreamObserver<com.image.grpc.ImgProto.ImageResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getImgProcessMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class imageBlockingStub extends io.grpc.stub.AbstractStub<imageBlockingStub> {
    private imageBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private imageBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected imageBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new imageBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.image.grpc.ImgProto.ImageResponse imgProcess(com.image.grpc.ImgProto.ImageRequest request) {
      return blockingUnaryCall(
          getChannel(), getImgProcessMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class imageFutureStub extends io.grpc.stub.AbstractStub<imageFutureStub> {
    private imageFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private imageFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected imageFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new imageFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.image.grpc.ImgProto.ImageResponse> imgProcess(
        com.image.grpc.ImgProto.ImageRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getImgProcessMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_IMG_PROCESS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final imageImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(imageImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_IMG_PROCESS:
          serviceImpl.imgProcess((com.image.grpc.ImgProto.ImageRequest) request,
              (io.grpc.stub.StreamObserver<com.image.grpc.ImgProto.ImageResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class imageBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    imageBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.image.grpc.ImgProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("image");
    }
  }

  private static final class imageFileDescriptorSupplier
      extends imageBaseDescriptorSupplier {
    imageFileDescriptorSupplier() {}
  }

  private static final class imageMethodDescriptorSupplier
      extends imageBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    imageMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (imageGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new imageFileDescriptorSupplier())
              .addMethod(getImgProcessMethod())
              .build();
        }
      }
    }
    return result;
  }
}
