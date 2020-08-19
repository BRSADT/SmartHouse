-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 16-08-2020 a las 02:58:19
-- Versión del servidor: 5.7.14
-- Versión de PHP: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `dulcehogar`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `configuracion`
--

CREATE TABLE `configuracion` (
  `usuarios_configuracion` varchar(100) NOT NULL,
  `ledCocina` int(11) NOT NULL,
  `ledSala` int(11) NOT NULL,
  `ventilador` int(11) NOT NULL,
  `ledCuarto11` int(11) NOT NULL,
  `ledCuarto12` int(11) NOT NULL,
  `ledCuarto2` int(11) NOT NULL,
  `tono` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `configuracion`
--

INSERT INTO `configuracion` (`usuarios_configuracion`, `ledCocina`, `ledSala`, `ventilador`, `ledCuarto11`, `ledCuarto12`, `ledCuarto2`, `tono`) VALUES
('ab45', 0, 1, 0, 0, 0, 0, 3),
('B5AD', 0, 0, 1, 0, 0, 0, 2),
('daniN2', 0, 1, 0, 0, 0, 1, 2),
('LIMA', 1, 1, 1, 1, 1, 1, 1),
('AB', 0, 0, 0, 0, 0, 0, 0),
('AB45', 0, 1, 0, 0, 0, 0, 3),
('1731', 0, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial`
--

CREATE TABLE `historial` (
  `Codigo` varchar(100) NOT NULL,
  `Hora` time NOT NULL,
  `Fecha` date NOT NULL,
  `TipodeAcceso` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `historial`
--

INSERT INTO `historial` (`Codigo`, `Hora`, `Fecha`, `TipodeAcceso`) VALUES
('asdasd', '07:32:18', '2019-06-05', 1),
('BSAD', '17:30:32', '2019-06-05', 1),
('asdasd', '07:32:18', '2019-06-05', 1),
('B5AD', '04:31:14', '2019-06-14', 1),
('Persona', '04:31:01', '2019-06-14', 1),
('invitado', '04:29:59', '2019-06-14', 1),
('Persona', '04:21:42', '2019-06-14', 1),
('B5AD', '04:21:14', '2019-06-14', 1),
('Persona', '04:20:14', '2019-06-14', 1),
('B5AD', '04:19:46', '2019-06-14', 1),
('AB45', '03:44:36', '2019-06-14', 1),
('AB45', '03:42:46', '2019-06-14', 1),
('AB45', '03:41:47', '2019-06-14', 1),
('Persona', '03:41:31', '2019-06-14', 1),
('Persona', '03:41:22', '2019-06-14', 1),
('AB45', '03:40:44', '2019-06-14', 1),
('Persona', '03:40:32', '2019-06-14', 1),
('invitado', '03:40:17', '2019-06-14', 1),
('B5AD', '03:33:53', '2019-06-14', 1),
('Persona', '03:33:30', '2019-06-14', 1),
('B5AD', '03:32:35', '2019-06-14', 1),
('Persona', '03:32:12', '2019-06-14', 1),
('B5AD', '03:31:10', '2019-06-14', 1),
('Persona', '03:31:00', '2019-06-14', 1),
('invitado', '03:30:46', '2019-06-14', 1),
('Persona', '03:30:34', '2019-06-14', 1),
('invitado', '03:29:40', '2019-06-14', 1),
('Persona', '03:29:27', '2019-06-14', 1),
('invitado', '03:29:18', '2019-06-14', 1),
('B5AD', '03:16:21', '2019-06-14', 1),
('Persona', '03:16:01', '2019-06-14', 1),
('invitado', '03:15:49', '2019-06-14', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `usuario` varchar(100) NOT NULL,
  `pass` varchar(100) NOT NULL,
  `administrador` varchar(100) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `Apellidos` varchar(100) NOT NULL,
  `Prioridad` varchar(100) NOT NULL,
  `Relacion` varchar(100) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`usuario`, `pass`, `administrador`, `nombre`, `Apellidos`, `Prioridad`, `Relacion`) VALUES
('B5AD', '123456', '1', 'Brenda Samantha', 'Avila De la torre', '1', ''),
('LIMA', '123456', '0', 'Luis Iván', 'Morett Arévalo', '2', ''),
('daniN2', '123', '0', 'Daniela', 'Rodriguez', '1', 'Hermana'),
('AB45', 'dabi', '0', 'dani', 'dabi', '1', 'hermana'),
('1731', 'Coss', '0', 'Rene', 'Coss', '1', 'hermano');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`usuario`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
