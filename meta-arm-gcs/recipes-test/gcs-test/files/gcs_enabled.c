// Copyright (C) 2024 Arm Ltd
// SPDX-License-Identifier: MIT

extern int gcs_enabled(void);

int main(void)
{
	return gcs_enabled() ? 0 : 1;
}
