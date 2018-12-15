package com.scraapp.network;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

class MockInterceptor implements LoggingInterceptor {

        String HEADER_SEPARATOR = ": ";
        private String HEADER_VALUE_ERROR_TRACKING_OK_HTTP = "HttpLoggingInterceptor";
        private String HEADER_KEY_REPORT_TO_USERPROFILE = "ReportErrorToUserProfile";
        private String HEADER_VALUE_ERROR_TRACKING_OK_HTTP_SUPPRESS_REQUEST_BODY = "OkHttpSuppressRequestBody";

        private static final int DEBUG_TRUCATE_MAX_CHARS = -1; //11000;

//    private final Context mContext;
        private static final String LOG_TAG = "okhttp";
        private static final Charset UTF8 = Charset.forName("UTF-8");
        private static final int INITIAL_STRINGBUILDER_SPACE = 1024;

        private StringBuilder reusableRequestErrorString = new StringBuilder(INITIAL_STRINGBUILDER_SPACE);
        private boolean reusableRequestErrorStringInUse = false;
        private StringBuilder reusableResponseErrorString = new StringBuilder(INITIAL_STRINGBUILDER_SPACE);
        private boolean reusableResponseErrorStringInUse = false;

        private final HttpLogger httpLogger;

        private volatile Level level = Level.NONE;

        @Override
        public void setLevel(Level level) {
            if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
            this.level = level;
        }

        @Override
        public void log(String tag, String msg, Throwable t) {

        }

    MockInterceptor() {
        this(HttpLogger.DEFAULT);
    }

   private MockInterceptor(HttpLogger httpLogger) {
            this.httpLogger = httpLogger;
        }

        public interface HttpLogger {
            void log(String message);
            void log(String tag, String message, Throwable t);

            HttpLogger DEFAULT = new HttpLogger() {
                @Override public void log(String message) {
                    log(LOG_TAG, message, null);
                    //Platform.get().log(message);
                }
                @Override public void log(String tag, String message, Throwable t) {
                    if (t == null) {
                        if (DEBUG_TRUCATE_MAX_CHARS > 0 && message != null && message.length() > DEBUG_TRUCATE_MAX_CHARS) {
                            message = message.substring(0, DEBUG_TRUCATE_MAX_CHARS - 1);
                        }
                        Log.e(tag, message);
                    } else {
                        Log.e(tag, message, t);
                    }
                }
            };
        }

//    MockInterceptor(Context context) {
//        mContext = context;
//    }
//
//    @Override
//    public Response intercept(@NonNull Chain chain) {
//        final Response response;
//        String responseString;
//        // Get Request URL pathSegments.
//        List<String> paths = chain.request().url().pathSegments();
//        final String endPoint = paths.get(paths.size() - 1);
//
//        try {
//            responseString = RestServiceMockUtils.getStringFromFile(mContext, endPoint);
//        } catch (IOException | Resources.NotFoundException e) {
//            responseString = "File is missing or failed to open it."; // this is malformed json string.
//            e.printStackTrace();
//        }
//        response = new Response.Builder()
//                .code(200)
//                .message(responseString)
//                .request(chain.request())
//                .protocol(Protocol.HTTP_1_1)
//                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
//                .addHeader("content-type", "application/json")
//                .build();
//        return response;
//    }


        @Override
        public Response intercept(Chain chain) throws IOException {
            Level level = this.level;

            Request request = chain.request();

            // NOTE: requestErrorString being null signifies that the error reporting will be skipped
            //       for this request.
            StringBuilder requestErrorString = null;
            List<String> reportErrorTrackers = request.headers(HEADER_KEY_REPORT_TO_USERPROFILE);
            boolean skipRequestBody = reportErrorTrackers != null && reportErrorTrackers.contains(HEADER_VALUE_ERROR_TRACKING_OK_HTTP_SUPPRESS_REQUEST_BODY);
            if (reportErrorTrackers != null && reportErrorTrackers.contains(HEADER_VALUE_ERROR_TRACKING_OK_HTTP)) {
                // Get allocation for gathering error reporting data.
                requestErrorString = getRequestStringBuilder();
            }

            boolean logNone = level == Level.NONE;
            boolean logBody = level == Level.BODY;
            boolean logHeaders = logBody || level == Level.HEADERS;

            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;

            Connection connection = chain.connection();
            Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

            String reqStart = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
            String requestStartMessage = reqStart;
            String reqAppend = "";
            if (hasRequestBody) {
                reqAppend = " (" + requestBody.contentLength() + "-byte body)";
                if (!logHeaders) {
                    requestStartMessage += reqAppend;
                }
            }

            addMessage(requestStartMessage, !logNone, null);
            appendErrorString(reqStart + reqAppend, requestErrorString);

            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    addMessage("Content-Type: " + requestBody.contentType(), logHeaders, requestErrorString);
                }
                if (requestBody.contentLength() != -1) {
                    addMessage("Content-Length: " + requestBody.contentLength(), logHeaders, requestErrorString);
                }
            }

            Headers requestHeaders = request.headers();
            for (int i = 0, count = requestHeaders.size(); i < count; i++) {
                String name = requestHeaders.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name) && !HEADER_KEY_REPORT_TO_USERPROFILE.equals(name)) {
                    addMessage(name + ": " + requestHeaders.value(i), logHeaders, requestErrorString);
                }
            }

            if (!hasRequestBody) {
                addMessage("--> END " + request.method(), logHeaders, requestErrorString);
            } else {
                addMessage("--> END " + request.method(), logHeaders && !logBody, null);

                if (bodyEncoded(request.headers())) {
                    addMessage("--> END " + request.method() + " (encoded body omitted)", logBody, requestErrorString);
                } else if (skipRequestBody) {
                    addMessage("--> END " + request.method() + " (sensitive body omitted)", logBody, requestErrorString);
                } else{
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }

                    addMessage("", logBody, null);
                    addMessage(buffer.readString(charset), logBody, requestErrorString);
                    addMessage("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)", logBody, requestErrorString);
                }
            }

            long startNs = System.nanoTime();
            Response response = chain.proceed(request);
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

            StringBuilder responseErrorString = null;
            if (response.code() < 400 || response.code() > 599) {
                resetErrorString(requestErrorString);
                requestErrorString = null;
                if (logNone) {
                    return response;
                }
            } else if (requestErrorString != null) { // Signifies that the error reporting is not skipped for this request
                responseErrorString = getResponseStringBuilder();
                Log.w(LOG_TAG, "4xx/5xx error in response. Collecting data for sending error string.");
            }

            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
            String resStart = "<-- " + response.code() + ' ' + response.message() + ' ' + response.request().url() + " (" + tookMs + "ms";
            String responseStartMessage = resStart;
            String resAppend = ", " + bodySize + " body";
            if (!logHeaders) {
                responseStartMessage += resAppend;
            }
            responseStartMessage += ")";

            addMessage(responseStartMessage, !logNone, null);
            appendErrorString(resStart + resAppend + ")", responseErrorString);

            Headers responseHeaders = response.headers();
            for (int i = 0, count = responseHeaders.size(); i < count; i++) {
                addMessage(responseHeaders.name(i) + ": " + responseHeaders.value(i), logHeaders, responseErrorString);
            }

            if (!HttpHeaders.hasBody(response)) {
                addMessage("<-- END HTTP", logHeaders, responseErrorString);
            } else {
                addMessage("<-- END HTTP", logHeaders && !logBody, null);

                if (bodyEncoded(response.headers())) {
                    addMessage("<-- END HTTP (encoded body omitted)", logBody, responseErrorString);
                } else {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        try {
                            charset = contentType.charset(UTF8);
                        } catch (UnsupportedCharsetException e) {
                            addMessage("", logBody, null);
                            addMessage("Couldn't decode the response body; charset is likely malformed.", logBody, responseErrorString);
                            addMessage("<-- END HTTP", logBody, responseErrorString);

                            if (requestErrorString != null || responseErrorString != null) {
                                response = addPayloads(requestErrorString, responseErrorString, response);
                            }
                            return response;
                        }
                    }

                    if (contentLength != 0) {
                        addMessage("", logBody, null);
                        addMessage(buffer.clone().readString(charset), logBody, responseErrorString);
                    }

                    addMessage("<-- END HTTP (" + buffer.size() + "-byte body)", logBody, responseErrorString);
                }
            }

            if (requestErrorString != null || responseErrorString != null) {
                response = addPayloads(requestErrorString, responseErrorString, response);
            }

            return response;
        }

        private void addMessage(String message, boolean addToLog, StringBuilder errorString) {
            appendErrorString(message, errorString);

            if (addToLog) {
                httpLogger.log(message);
            }
        }

        private void appendErrorString(String error, StringBuilder errorString) {
            if (errorString == null) {
                return;
            }

            synchronized (errorString) {
                try {
                    if (errorString.length() != 0) {
                        error = "\n" + error;
                    }
                    errorString.append(error);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error appending string builder data", e);
                }
            }
        }

        private void resetErrorString(StringBuilder errorString) {
            if (errorString == null) {
                return;
            }

            synchronized (errorString) {
                errorString.setLength(0);
                if (errorString == reusableRequestErrorString) {
                    reusableRequestErrorStringInUse = false;
                    if (reusableRequestErrorString.capacity() > INITIAL_STRINGBUILDER_SPACE) {
                        reusableRequestErrorString = new StringBuilder(INITIAL_STRINGBUILDER_SPACE);
                    }
                } else if (errorString == reusableResponseErrorString) {
                    reusableResponseErrorStringInUse = false;
                    if (reusableResponseErrorString.capacity() > INITIAL_STRINGBUILDER_SPACE) {
                        reusableResponseErrorString = new StringBuilder(INITIAL_STRINGBUILDER_SPACE);
                    }
                }
            }
        }

        private Response addPayloads (StringBuilder requestErrorString, StringBuilder responseErrorString, Response response) {
            Response.Builder builder = response.newBuilder();
            if (requestErrorString != null) {
//            Logger.d("HttpLoggingInterceptor", "Request string = " + requestErrorString);
                try {
                    builder.addHeader("OkHttpRequest", Base64.encodeToString(requestErrorString.toString().getBytes("UTF-8"), Base64.NO_WRAP));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (responseErrorString != null) {
//            Logger.d("HttpLoggingInterceptor", "Response string = " + responseErrorString);
                try {
                    builder.addHeader("OkHttpResponse", Base64.encodeToString(responseErrorString.toString().getBytes("UTF-8"), Base64.NO_WRAP));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return builder.build();
        }

        private boolean bodyEncoded(Headers headers) {
            String contentEncoding = headers.get("Content-Encoding");
            return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
        }

        private StringBuilder getResponseStringBuilder() {
            synchronized (reusableResponseErrorString) {
                if (!reusableResponseErrorStringInUse) {
                    reusableResponseErrorStringInUse = true;
                    return reusableResponseErrorString;
                }
                return new StringBuilder(INITIAL_STRINGBUILDER_SPACE);
            }
        }

        @NonNull
        private StringBuilder getRequestStringBuilder() {
            synchronized (reusableRequestErrorString) {
                if (!reusableRequestErrorStringInUse) {
                    reusableRequestErrorStringInUse = true;
                    return reusableRequestErrorString;
                }
                return new StringBuilder(INITIAL_STRINGBUILDER_SPACE);
            }
        }

    }


//    private final Context mContext;
//
//    MockInterceptor(Context context) {
//        mContext = context;
//    }
//
//    @Override
//    public Response intercept(@NonNull Chain chain) {
//        final Response response;
//        String responseString;
//        // Get Request URL pathSegments.
//        List<String> paths = chain.request().url().pathSegments();
//        final String endPoint = paths.get(paths.size() - 2);
//
//        try {
//            responseString = RestServiceMockUtils.getStringFromFile(mContext, endPoint );
//        } catch (IOException | Resources.NotFoundException e) {
//            responseString = "File is missing or failed to open it."; // this is malformed json string.
//            e.printStackTrace();
//        }
//        response = new Response.Builder()
//                .code(200)
//                .message(responseString)
//                .request(chain.request())
//                .protocol(Protocol.HTTP_1_1)
//                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
//                .addHeader("content-type", "application/json")
//                .build();
//        return response;
//    }

