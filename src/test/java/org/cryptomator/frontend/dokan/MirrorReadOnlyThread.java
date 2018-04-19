package org.cryptomator.frontend.dokan;

import com.dokany.java.DokanyDriver;
import com.dokany.java.DokanyFileSystem;
import com.dokany.java.constants.FileSystemFeature;
import com.dokany.java.constants.MountOption;
import com.dokany.java.structure.DeviceOptions;
import com.dokany.java.structure.EnumIntegerSet;
import com.dokany.java.structure.FreeSpace;
import com.dokany.java.structure.VolumeInformation;

import java.nio.file.Path;

public class MirrorReadOnlyThread implements Runnable {

	private static final long TIMEOUT = 1000;

	private final Path mountPoint;
	private final Path dirToMirror;
	private final DeviceOptions devOps;
	private final DokanyDriver dokany;

	public MirrorReadOnlyThread(Path dirToMirror, Path mountPoint) {
		System.out.println("Initializing Dokany MirrorFS with MountPoint " + mountPoint.toString() + " and directory to mirror " + dirToMirror.toString());
		this.mountPoint = mountPoint;
		this.dirToMirror = dirToMirror;

		final short threadCount = 1;
		EnumIntegerSet mountOptions = new EnumIntegerSet<>(MountOption.class);
		mountOptions.add(MountOption.DEBUG_MODE, MountOption.STD_ERR_OUTPUT, MountOption.MOUNT_MANAGER);
		String uncName = "";
		int timeout = 10000;
		int allocationUnitSize = 4096;
		int sectorSize = 4096;

		devOps = new DeviceOptions(mountPoint.toString(), threadCount, mountOptions, uncName, timeout, allocationUnitSize, sectorSize);

		EnumIntegerSet fsFeatures = new EnumIntegerSet<>(FileSystemFeature.class);
		fsFeatures.add(FileSystemFeature.CASE_PRESERVED_NAMES, FileSystemFeature.CASE_SENSITIVE_SEARCH,
				FileSystemFeature.PERSISTENT_ACLS, FileSystemFeature.SUPPORTS_REMOTE_STORAGE, FileSystemFeature.UNICODE_ON_DISK);

		VolumeInformation volumeInfo = new VolumeInformation(VolumeInformation.DEFAULT_MAX_COMPONENT_LENGTH, "Mirror", 0x98765432, "Dokany MirrorFS", fsFeatures);
		FreeSpace freeSpace = new FreeSpace(200000, 200);

		DokanyFileSystem myFs = new ReadOnlyAdapter(dirToMirror, volumeInfo, freeSpace);
		dokany = new DokanyDriver(devOps, myFs);
	}

	@Override
	public void run() {
		dokany.start();
		System.out.println("Starting new dokany thread with mount point " + mountPoint.toString());
	}

	public DokanyDriver getDokanyDriver() {
		return dokany;
	}
}