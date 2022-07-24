/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.api.vo

/**
 * The status of a message, used on StatusVO and FragmentStatus
 */
enum class Status {
    DELIVERED,
    SENT,
    SENDING,
    FAILED,
    UNKNOWN;
}