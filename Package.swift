// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "KmmSharedSpm",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "KmmSharedSpm",
            targets: ["KmmSharedSpm"]),
    ],
    dependencies: [
        // no dependencies
    ],
    targets: [
        .binaryTarget(
            name: "KmmSharedSpm",
            url: "https://github.com/ankush-keka/kmm-sample/releases/download/0.0.4-snapshot/KmmSharedSpm.xcframework.zip",
            checksum: "7a0be156c17c7469cceb2fbbd0b1777187247fb48b44c301ce7c41dc64e0a50c"
        ),
    ]
)
