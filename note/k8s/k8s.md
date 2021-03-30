


- Kubernetes
    - Kubernetes 是一个可移植的、可扩展的开源平台，用于管理容器化的工作负载和服务，可促进声明式配置和自动化
    - 容器是打包和运行应用程序的好方式。在生产环境中，你需要管理运行应用程序的容器，并确保不会停机。 例如，如果一个容器发生故障，则需要启动另一个容器。如果系统处理此行为，会不会更容易？
        - 这就是 Kubernetes 来解决这些问题的方法！ Kubernetes 为你提供了一个可弹性运行分布式系统的框架。 Kubernetes 会满足你的扩展要求、故障转移、部署模式等
        - 包括部署，调度和节点集群间扩展等
        
    - Kubernetes 是一款基础设施工具，可对多种不同计算资源（例如虚拟 / 物理机）进行分组，使其呈现为统一的巨量计算资源，从而供应用程序使用或与其他人共享
    - 在这样的架构中，Docker（或者容器运行时）仅用于通过 Kubernetes 控制平面进行调度，从而在实际主机内运行应用程序
    - Kubernetes 是今天容器编排领域的事实标准，而 Docker 从诞生之日到今天都在容器中扮演着举足轻重的地位，也都是 Kubernetes 中的默认容器引擎
    - 一个用于大规模运行分布式应用和服务的开源容器编排平台
- Kubernetes 集群
    - 把一个有效的 Kubernetes 部署称为集群。您可以将 Kubernetes 集群可视化为两个部分：控制平面与计算设备（或称为节点）。每个节点都是其自己的 Linux® 环境，并且可以是物理机或虚拟机
    - 控制平面
        - K8s 集群的神经中枢
        - 控制集群的 Kubernetes 组件以及一些有关集群状态和配置的数据，这些核心 Kubernetes 组件负责处理重要的工作，以确保容器以足够的数量和所需的资源运行
        - 集群已被配置为以特定的方式运行，而控制平面要做的就是确保万无一失
    - 计算设备（或称为节点）
        - Kubernetes 集群中至少需要一个计算节点，但通常会有多个计算节点。容器集经过调度和编排后，就会在节点上运行。如果需要扩展集群的容量，那就要添加更多的节点
        - 为了运行容器，每个计算节点都有一个容器运行时引擎。比如 Docker，但 Kubernetes 也支持其他符合开源容器运动（OCI）标准的运行时
- Node
    - Kubernetes 通过将容器放入在节点（Node）上运行的 Pod 中来执行你的工作负载。 节点可以是一个虚拟机或者物理机器，取决于所在的集群配置。 每个节点包含运行 Pods 所需的服务， 这些 Pods 由 控制面 负责管理
    - 节点上的组件包括 kubelet、 容器运行时以及 kube-proxy。
    - 集群是一组节点，这些节点可以是物理服务器或者虚拟机，之上安装了Kubernetes平台
    - 为了运行容器，每个计算节点都有一个容器运行时引擎。比如 Docker，但 Kubernetes 也支持其他符合开源容器运动（OCI）标准的运行时，例如 rkt 和 CRI-O
- Kubelet
    - 每个计算节点中都包含一个 kubelet，这是一个与控制平面通信的微型应用。当控制平面需要在节点中执行某个操作时，kubelet 就会执行该操作
    - kubernetes 是一个分布式的集群管理系统，在每个节点（node）上都要运行一个 worker 对容器进行生命周期的管理，这个 worker 程序就是 kubelet
    - 简单地说，kubelet 的主要功能就是定时从某个地方获取节点上 pod/container 的期望状态（运行什么容器、运行的副本数量、网络或者存储如何配置等等），并调用对应的容器平台接口达到这个状态。
    - 主要功能
        - pod 管理
        - 容器健康检查
        - 容器监控
    - 各容器化应用程序作为 kubelet 通过 IPC 在 gRPC 内通信，而且运行时也运行在同一主机之上
          
- Pod
    - 在 kubernetes 的设计中，最基本的管理单位是 pod，而不是 container。pod 是 kubernetes 在容器上的一层封装，由一组运行在同一主机的一个或者多个容器组成。如果把容器比喻成传统机器上的一个进程（它可以执行任务，对外提供某种功能），那么 pod 可以类比为传统的主机：它包含了多个容器，为它们提供共享的一些资源
    - 主要是因为容器推荐的用法是里面只运行一个进程，而一般情况下某个应用都由多个组件构成的
    - pod 中所有的容器最大的特性也是最大的好处就是共享了很多资源，比如网络空间。pod 下所有容器共享网络和端口空间，也就是它们之间可以通过 localhost 访问和通信，对外的通信方式也是一样的，省去了很多容器通信的麻烦
    -  Kubernetes 直接管理 Pod，而不是容器, Pod 可看作是容器的包装器
    - 除了 Docker 之外，Kubernetes 支持 很多其他容器运行时， Docker 是最有名的运行时， 使用 Docker 的术语来描述 Pod 会很有帮助
    - Pod 的共享上下文包括一组 Linux 名字空间、控制组（cgroup）和可能一些其他的隔离 方面，即用来隔离 Docker 容器的技术。 在 Pod 的上下文中，每个独立的应用可能会进一步实施隔离
    - 就 Docker 概念的术语而言，Pod 类似于共享名字空间和文件系统卷的一组 Docker 容器。
    - Kubernetes 集群中的 Pod 主要有两种用法：
        - 运行单个容器的 Pod。"每个 Pod 一个容器"模型是最常见的 Kubernetes 用例； 在这种情况下，可以将 Pod 看作单个容器的包装器，并且 Kubernetes 直接管理 Pod，而不是容器
        - 运行多个协同工作的容器的 Pod。 Pod 可能封装由多个紧密耦合且需要共享资源的共处容器组成的应用程序。 这些位于同一位置的容器可能形成单个内聚的服务单元 —— 一个容器将文件从共享卷提供给公众， 而另一个单独的“挂斗”（sidecar）容器则刷新或更新这些文件。 Pod 将这些容器和存储资源打包为一个可管理的实体
            - 将多个并置、同管的容器组织到一个 Pod 中是一种相对高级的使用场景。 只有在一些场景中，容器之间紧密关联时你才应该使用这种模式

- CRI
    - 容器运行时接口, Kubernetes 运行时 API
    - Docker 并不支持 CRI（容器运行时接口）这一 Kubernetes 运行时 API，而 Kubernetes 用户一直以来所使用的其实是名为“dockershim”的桥接服务
    - Dockershim 能够转换 Docker API 与 CRI
    - Kubelet 之前使用的是一个名为 dockershim 的模块，用以实现对 Docker 的 CRI 支持。
    - Kubernetes 只能与 CRI 通信，因此要与 Docker 通信，就必须使用桥接服务
    - CRI 是 Kubernetes 提供的 API，用于同容器运行时进行通信以创建 / 删除容器化应用程序
    - Kubernetes 引入容器运行时接口（Container Runtime Interface、CRI）隔离不同容器运行时的实现机制
    - CRI 是一系列用于管理容器运行时和镜像的 gRPC 接口
    
- 容器运行时(CRI 运行时)
    - Kubelet 通过 Container Runtime Interface (CRI) 与容器运行时交互，以管理镜像和容器
    - 容器运行时（Container Runtime）是 Kubernetes（k8s） 最重要的组件之一，负责管理镜像和容器的生命周期
    - containerd 和 docker 作为运行时组件的区别
        - docker    
            - 可以调用 docker API
            - 可以使用 docker compose 
            - Docker 作为 k8s 容器运行时，调用关系
                - kubelet --> docker shim （在 kubelet 进程中） --> dockerd(Docker 守护进程) --> containerd
            - docker 对容器的管理和操作基本都是通过 containerd 完成的
                - 为什么需要独立的 containerd
                    - 继续从整体 docker 引擎中分离出的项目(开源项目的思路)
                    - 可以被 Kubernets CRI 等项目使用(通用化)
                    - 为广泛的行业合作打下基础(就像 runC 一样)
            - Kubernetes 中的节点代理 Kubelet 为了访问 Docker 提供的服务需要先经过社区维护的 Dockershim，Dockershim 会将请求转发给管理容器的 Docker 服务
            - Docker Engine -> containerd -> shim -> runC
        - containerd
            - containerd提供的 CRI 其实 100% 就是由 Docker 所提供
            - Containerd 可以在宿主机中管理完整的容器生命周期：容器镜像的传输和存储、容器的执行和管理、存储和网络等
            - Containerd 调用链更短，组件更少，更稳定，占用节点资源更少
            - Containerd 作为 k8s 容器运行时，调用关系
                - kubelet --> cri plugin（在 containerd 进程中） --> containerd
        - Containerd 和 Docker 组件常用命令不同
            - Containerd 不支持 docker API 和 docker CLI，但是可以通过 cri-tool 命令实现类似的功能
            - 镜像相关功能	
                - 如 docker images、 crictl images
            - 容器相关功能	
                - 如 docker ps、crictl ps
            - POD 相关功能	
                - Docker无 、如 crictl pods
        - CRI-O
            - 不依赖于 Docker
            - CRI-O 的优势在于其采用极简风格，或者说它的设计本身就是作为“纯 CRI”运行时存在。不同于作为 Docker 组成部分的 containerd，CRI-O 在本质上属于纯 CRI 运行时、因此不包含除 CRI 之外的任何其他内容
    - CRI 运行时负责从 kubelet 获取请求并执行 OCI 容器运行时以运行容器
    - 从 kubelet 获取 gRPC 请求。根据规范创建 OCIjson 配置
      
- OCI
    - Open Container Initiative
    - The Open Container Initiative is an open governance structure for the express purpose of creating open industry standards around container formats and runtimes.
    - OCI 定义了容器运行时标准，runC 是 Docker 按照开放容器格式标准（OCF, Open Container Format）制定的一种具体实现
    
    
- OCI 运行时
    - OCI 运行时负责使用 Linux 内核系统调用（例如 cgroups 与命名空间）生成容器
    - runC
        - CRI 会通过 Linux 系统调用以执行二进制文件，而后 runC 生成容器。这表明 runC 依赖于 Linux 计算机上运行的内核
    - gVisor
                   
