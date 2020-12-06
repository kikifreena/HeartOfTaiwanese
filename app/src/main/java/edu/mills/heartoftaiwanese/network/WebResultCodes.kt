package edu.mills.heartoftaiwanese.network

/**
 * Class to contain all the types of errors
 * Invalid: When a search was successful, but nothing was found
 * Rate limited: Too many requests error.
 * OK: Success
 * Unknown: Something else happened
 */
enum class WebResultCodes {
    RESULT_OK,
    UNKNOWN_ERROR,
    RATE_LIMITED,
    INVALID_NOT_FOUND
}