package logger

import (
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
	"gopkg.in/natefinch/lumberjack.v2"
	"os"
	"server/pkg/conf"
)

var sLog *zap.SugaredLogger

// 使用第三方文件分割库
func getFileWriter() zapcore.WriteSyncer {
	l := &lumberjack.Logger{
		Filename:   "./logs/server.log",
		MaxSize:    10, //Mbytes
		MaxAge:     30, //days
		MaxBackups: 5,
		LocalTime:  true,
		Compress:   false,
	}
	return zapcore.AddSync(l)
}

func getEncoder() zapcore.Encoder {
	encoderConfig := zap.NewProductionEncoderConfig()
	encoderConfig.EncodeTime = zapcore.ISO8601TimeEncoder
	encoderConfig.EncodeLevel = zapcore.CapitalLevelEncoder
	return zapcore.NewConsoleEncoder(encoderConfig)
}

// 根据服务运行的级别加载不同的zap core
func initZapCore() (coreMap []zapcore.Core) {
	encoder := getEncoder()
	fileWriteSyncer := getFileWriter()
	//服务运行模式为debug时打印输出level为DEBUG以上并保存
	if conf.ServerConf.Mode == "debug" {
		coreMap = append(coreMap, zapcore.NewCore(encoder, zapcore.AddSync(os.Stdout), zapcore.DebugLevel))
		coreMap = append(coreMap, zapcore.NewCore(encoder, zapcore.AddSync(fileWriteSyncer), zapcore.DebugLevel))
	} else {
		coreMap = append(coreMap, zapcore.NewCore(encoder, zapcore.AddSync(fileWriteSyncer), zap.WarnLevel))
	}
	return
}

func New() {
	core := zapcore.NewTee(
		initZapCore()...,
	)
	caller := zap.AddCaller()
	//设置跳过调用者
	skip := zap.AddCallerSkip(1)
	logger := zap.New(core, caller, skip)
	//替换全局Logger====>zap.L()
	zap.ReplaceGlobals(logger)
	sLog = zap.L().Sugar()
}

func Infoln(args ...interface{}) {
	sLog.Infoln(args)
}

func Infof(t string, args ...interface{}) {
	sLog.Infof(t, args)
}

func Debugln(args ...interface{}) {
	sLog.Debugln(args)
}

func Debugf(t string, args ...interface{}) {
	sLog.Debugf(t, args)
}

func Warnln(args ...interface{}) {
	sLog.Warnln(args)
}

func Warnf(t string, args ...interface{}) {
	sLog.Warnf(t, args)
}

func Errorln(args ...interface{}) {
	sLog.Errorln(args)
}

func Errorf(t string, args ...interface{}) {
	sLog.Errorf(t, args)
}

func Fatal(args ...interface{}) {
	sLog.Fatal(args)
}

func Fatalf(t string, args ...interface{}) {
	sLog.Fatalf(t, args)
}
