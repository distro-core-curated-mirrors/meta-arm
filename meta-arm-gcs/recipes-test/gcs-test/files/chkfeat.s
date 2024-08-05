/* Copyright (C) 2024 Arm Ltd */
/* SPDX-License-Identifier: MIT */

.global gcs_enabled
.type gcs_enabled,@function
gcs_enabled:
	mov x16, #0x1
	hint 40
	eor x0, x16, #0x1
	ret

.pushsection .note.gnu.property, "a"
.balign 8
.long 4
.long 0x10
.long 0x5
.asciz "GNU"
.long 0xc0000000
.long 4
.long 4
.long 0
.popsection
