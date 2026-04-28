// IInterAppService.aidl
// Package must match the service implementation package exactly.
package com.example.android.playground.interappcomm;

/**
 * AIDL interface for the inter-app communication AIDL demo.
 *
 * Both the "default" and "variant" apps compile THIS file — they share identical
 * Binder transaction codes which is the prerequisite for cross-process calls.
 *
 * Key concepts shown here:
 *  - Regular methods are SYNCHRONOUS — the caller blocks on the Binder thread pool
 *    until the server returns. Use 'oneway' to make a method fire-and-forget.
 *  - All method arguments are marshalled through Parcel — only AIDL-supported
 *    types (primitives, String, List<T>, Parcelable) are allowed.
 *  - The server implementation MUST be thread-safe (Binder may call methods
 *    concurrently from multiple threads in its thread pool).
 */
interface IInterAppService {

    /**
     * Synchronous ping — caller sends a message, server returns it echoed back
     * with a prefix showing which app is running the service.
     */
    String ping(String message);

    /**
     * Returns the list of messages currently stored on the server side.
     * Demonstrates a typed List<String> return — AIDL supports List<String> natively.
     */
    List<String> getMessages();

    /**
     * Posts a message into the server's in-memory store.
     * 'oneway' makes this ASYNCHRONOUS — caller does NOT block for completion.
     * There is no return value and no guaranteed delivery order from the caller's view.
     */
    oneway void postMessage(String content, String senderPackage);
}
